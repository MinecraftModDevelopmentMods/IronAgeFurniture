package zone.moddev.mc.ironagefurniture.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CandleFlameParticle extends Particle {
    private final float flameScale;

    private CandleFlameParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float scale) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);

        this.motionX *= 0.01D;
        this.motionY *= 0.01D;
        this.motionZ *= 0.01D;
        this.posX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.02F);
        this.posY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.02F);
        this.posZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.02F);
        this.flameScale = this.particleScale * scale;
        this.particleScale = this.flameScale;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.particleMaxAge = (int)(7.0D / (Math.random() * 0.8D + 0.2D)) + 3;
        this.setParticleTextureIndex(48);
    }

    public static void spawn(World worldIn, double x, double y, double z) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new CandleFlameParticle(worldIn, x, y, z, 0.45F));
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    @Override
    public void renderParticle(VertexBuffer buffer, Entity entityIn, float partialTicks, float rotationX,
            float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float ageRatio = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0F - ageRatio * ageRatio * 0.5F);
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        float ageRatio = ((float)this.particleAge + partialTick) / (float)this.particleMaxAge;
        ageRatio = MathHelper.clamp(ageRatio, 0.0F, 1.0F);
        int brightness = super.getBrightnessForRender(partialTick);
        int low = brightness & 255;
        int high = brightness >> 16 & 255;
        low = low + (int)(ageRatio * 15.0F * 16.0F);

        if (low > 240) {
            low = 240;
        }

        return low | high << 16;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.96D;
        this.motionY *= 0.96D;
        this.motionZ *= 0.96D;
    }
}
