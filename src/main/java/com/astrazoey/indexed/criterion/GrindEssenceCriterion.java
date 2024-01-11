package com.astrazoey.indexed.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class GrindEssenceCriterion extends AbstractCriterion<GrindEssenceCriterion.Conditions> {

    static final Identifier ID = new Identifier("grind_essence");

    @Override
    public Identifier getId() {
        return ID;
    }

    public GrindEssenceCriterion.Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new GrindEssenceCriterion.Conditions(playerPredicate);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (conditions) -> {
            return conditions.matches(player);
        });
    }


    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(LootContextPredicate player) {
            super(GrindEssenceCriterion.ID, player);
        }

        public static GrindEssenceCriterion.Conditions create() {
            return new GrindEssenceCriterion.Conditions(LootContextPredicate.EMPTY);
        }

        public boolean matches(ServerPlayerEntity player) {
            return true;
        }
    }
}
