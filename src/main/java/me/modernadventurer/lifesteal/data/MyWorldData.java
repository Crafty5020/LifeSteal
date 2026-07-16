package me.modernadventurer.lifesteal.data;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

public class MyWorldData extends PersistentState {
    private boolean EndPortalSpanwed = false;
    private BlockPos portalPos = BlockPos.ORIGIN;
    public static final Codec<MyWorldData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.fieldOf("portalPos").forGetter(MyWorldData::getPortalPos),
                    Codec.BOOL.fieldOf("EndPortalSpanwed").forGetter(MyWorldData::getEndPortalSpanwed)

            ).apply(instance, (pos, enabled) -> {
                MyWorldData data = new MyWorldData();
                data.setPortalPos(pos);
                data.setEndPortalSpanwed(enabled);
                return data;
            })
    );

    public BlockPos getPortalPos() { return portalPos; }


    public void setPortalPos(BlockPos pos) {
        this.portalPos = pos;
        this.markDirty();
    }

    public boolean getEndPortalSpanwed() {
        return EndPortalSpanwed;
    }
    public void setEndPortalSpanwed(boolean spanwed) {
        this.EndPortalSpanwed = spanwed;
        this.markDirty();
    }
}
