package com.pidaw.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "El título de la tarea es obligatorio")
    private String title;


    private String description;

    @NotNull(message = "El estado completado es obligatorio")
    @Builder.Default
    private boolean completed =false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDate deadline;

    @NotNull(message = "La prioridad es obligatoria")
    public enum Priority { BAJA, MEDIA, ALTA }
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name="category_id",referencedColumnName = "id",foreignKey =@ForeignKey(name= "fk_task_category"))
    private Category category;

    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="task_tag",
            joinColumns = @JoinColumn(name="task_id"),
            foreignKey = @ForeignKey(name="fk_task_tag_task"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            inverseForeignKey = @ForeignKey(name="fk_task_tag_tag")
    )

    @Setter(AccessLevel.NONE)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "author_id",foreignKey = @ForeignKey(name="fk_task_user"))
    private User author;

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Task task = (Task) o;
        return getId() != null && Objects.equals(getId(), task.getId());
    }
}
