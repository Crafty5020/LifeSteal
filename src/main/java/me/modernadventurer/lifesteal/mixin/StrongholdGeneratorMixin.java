package me.modernadventurer.lifesteal.mixin;

import net.minecraft.structure.StrongholdGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(StrongholdGenerator.class)
public class StrongholdGeneratorMixin {
    @Shadow
    @Mutable
    private static List<StrongholdGenerator.PieceData> possiblePieces;

    @Inject(method = "init", at = @At("TAIL"))
    private static void onInit(CallbackInfo ci) {
        // Remove the PortalRoom from the list of pieces allowed to generate
        if (possiblePieces != null) {
            possiblePieces.removeIf(pieceData ->
                    pieceData.pieceType.equals(StrongholdGenerator.PortalRoom.class)
            );
        }
    }
}

