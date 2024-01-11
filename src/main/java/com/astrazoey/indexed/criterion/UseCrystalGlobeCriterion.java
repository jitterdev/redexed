package com.astrazoey.indexed.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class UseCrystalGlobeCriterion extends AbstractCriterion<UseCrystalGlobeCriterion.Conditions> {

    static final Identifier ID = new Identifier("use_crystal_globe");

    @Override
    public Identifier getId() {
        return ID;
    }

    public UseCrystalGlobeCriterion.Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new UseCrystalGlobeCriterion.Conditions(playerPredicate);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (conditions) -> {
            return conditions.matches(player);
        });
    }


    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(LootContextPredicate player   ) {
            super(UseCrystalGlobeCriterion.ID, player);
        }

        public static UseCrystalGlobeCriterion.Conditions create() {
            return new UseCrystalGlobeCriterion.Conditions(LootContextPredicate.EMPTY);
        }

        public boolean matches(ServerPlayerEntity player) {
            return true;
        }
    }
}
