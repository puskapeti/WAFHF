package com.example.wafhfbackend.entities

import javax.persistence.*

@Entity
class CookPlan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val cookPlanId: Long? = null,
    @OneToOne(cascade = [CascadeType.ALL])
    private val recipe: Recipe,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cook_plan_steps", joinColumns = [JoinColumn(name = "cookPlanId")])
    private val steps: Set<String>
) {
    override fun toString(): String {
        return "CookPlan[$cookPlanId]: $recipe, steps: $steps"
    }
}