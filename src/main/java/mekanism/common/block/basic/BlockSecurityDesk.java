package mekanism.common.block.basic;

import java.util.UUID;
import javax.annotation.Nonnull;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeGui;
import mekanism.common.block.machine.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.tile.TileEntitySecurityDesk;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.SecurityUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockSecurityDesk extends BlockTileModel<TileEntitySecurityDesk, BlockTypeTile<TileEntitySecurityDesk>> {

    public BlockSecurityDesk() {
        super(MekanismBlockTypes.SECURITY_DESK);
    }

    @Override
    public void setTileData(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, TileEntityMekanism tile) {
        if (tile instanceof TileEntitySecurityDesk && placer != null) {
            ((TileEntitySecurityDesk) tile).ownerUUID = placer.getUniqueID();
        }
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntitySecurityDesk tile = MekanismUtils.getTileEntity(TileEntitySecurityDesk.class, world, pos);
        if (tile != null) {
            if (!player.isSneaking()) {
                if (!world.isRemote) {
                    UUID ownerUUID = tile.ownerUUID;
                    if (ownerUUID == null || player.getUniqueID().equals(ownerUUID)) {
                        NetworkHooks.openGui((ServerPlayerEntity) player, Attribute.get(this, AttributeGui.class).getProvider(tile), pos);
                    } else {
                        SecurityUtils.displayNoAccess(player);
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}