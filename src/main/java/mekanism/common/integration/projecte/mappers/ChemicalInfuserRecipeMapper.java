package mekanism.common.integration.projecte.mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mekanism.api.annotations.NonNull;
import mekanism.api.gas.GasStack;
import mekanism.api.recipes.ChemicalInfuserRecipe;
import mekanism.common.integration.projecte.NSSGas;
import mekanism.common.recipe.MekanismRecipeType;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.mapper.recipe.IRecipeTypeMapper;
import moze_intel.projecte.api.mapper.recipe.RecipeTypeMapper;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

@RecipeTypeMapper
public class ChemicalInfuserRecipeMapper implements IRecipeTypeMapper {

    @Override
    public String getName() {
        return "MekChemicalInfuser";
    }

    @Override
    public String getDescription() {
        return "Maps Mekanism chemical infuser recipes.";
    }

    @Override
    public boolean canHandle(IRecipeType<?> recipeType) {
        return recipeType == MekanismRecipeType.CHEMICAL_INFUSING;
    }

    @Override
    public boolean handleRecipe(IMappingCollector<NormalizedSimpleStack, Long> mapper, IRecipe<?> iRecipe) {
        if (!(iRecipe instanceof ChemicalInfuserRecipe)) {
            //Double check that we have a type of recipe we know how to handle
            return false;
        }
        ChemicalInfuserRecipe recipe = (ChemicalInfuserRecipe) iRecipe;
        List<@NonNull GasStack> leftInputRepresentations = recipe.getLeftInput().getRepresentations();
        List<@NonNull GasStack> rightInputRepresentations = recipe.getRightInput().getRepresentations();
        for (GasStack leftRepresentation : leftInputRepresentations) {
            NormalizedSimpleStack nssLeft = NSSGas.createGas(leftRepresentation);
            for (GasStack rightRepresentation : rightInputRepresentations) {
                Map<NormalizedSimpleStack, Integer> ingredientMap = new HashMap<>();
                ingredientMap.put(nssLeft, leftRepresentation.getAmount());
                ingredientMap.put(NSSGas.createGas(rightRepresentation), rightRepresentation.getAmount());
                GasStack output = recipe.getOutput(leftRepresentation, rightRepresentation);
                mapper.addConversion(output.getAmount(), NSSGas.createGas(output), ingredientMap);
            }
        }
        return true;
    }
}