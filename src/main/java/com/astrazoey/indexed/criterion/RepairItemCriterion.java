package com.astrazoey.indexed.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RepairItemCriterion extends AbstractCriterion<RepairItemCriterion.Conditions> {

    static final Identifier ID = new Identifier("repair_item");

    @Override
    public Identifier getId() {
        return ID;
    }

    public RepairItemCriterion.Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new RepairItemCriterion.Conditions(playerPredicate);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (conditions) -> {
            return conditions.matches(player);
        });
    }


    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(LootContextPredicate player) {
            super(RepairItemCriterion.ID, player);
        }

        public static RepairItemCriterion.Conditions create() {
            return new RepairItemCriterion.Conditions(LootContextPredicate.EMPTY);
        }

        public boolean matches(ServerPlayerEntity player) {
            return true;
        }
    }
}
