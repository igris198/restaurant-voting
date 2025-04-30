package ru.javaops.bootjava.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "meal")
public class Meal extends AbstractNamedEntity {
    @Column(name = "price", nullable = false)
    @NotNull
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Menu menu;

    public Meal() {
    }

    public Meal(Integer id, String name, BigDecimal price) {
        super(id, name);
        this.price = price;
    }

    public void setPrice(@NotNull BigDecimal price) {
        this.price = price;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public @NotNull BigDecimal getPrice() {
        return price;
    }

    public Menu getMenu() {
        return menu;
    }
}
