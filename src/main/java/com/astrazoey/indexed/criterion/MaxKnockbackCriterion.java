package com.astrazoey.indexed.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MaxKnockbackCriterion extends AbstractCriterion<MaxKnockbackCriterion.Conditions> {

    static final Identifier ID = new Identifier("max_knockback");

    @Override
    public Identifier getId() {
        return ID;
    }

    public MaxKnockbackCriterion.Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new MaxKnockbackCriterion.Conditions(playerPredicate);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (conditions) -> {
            return conditions.matches(player);
        });
    }


    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(LootContextPredicate player) {
            super(MaxKnockbackCriterion.ID, player);
        }

        public static MaxKnockbackCriterion.Conditions create() {
            return new MaxKnockbackCriterion.Conditions(LootContextPredicate.EMPTY);
        }

        public boolean matches(ServerPlayerEntity player) {
            return true;
        }
    }
}
