package org.polyfrost.vanillahud.mixin;

import cc.polyfrost.oneconfig.libs.universal.UResolution;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.renderer.GlStateManager;
import org.polyfrost.vanillahud.hud.Hotbar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GuiSpectator.class)
public abstract class GuiSpectatorMixin {

    @ModifyArg(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSpectator;func_175258_a(Lnet/minecraft/client/gui/ScaledResolution;FIFLnet/minecraft/client/gui/spectator/categories/SpectatorDetails;)V"), index = 3)
    private float y(float f) {
        if (!Hotbar.hud.isEnabled()) return f;
        return UResolution.getScaledHeight() - 22;
    }

    @ModifyArgs(method = "func_175258_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSpectator;drawTexturedModalRect(FFIIII)V"))
    private void setPosition(Args args) {
        if (!Hotbar.hud.isEnabled()) return;
        int x = UResolution.getScaledWidth() / 2 - 91;
        int y = UResolution.getScaledHeight() - 22;
        args.set(0, ((float) args.get(0)) - x);
        args.set(1, ((float) args.get(1)) - y);
    }

    @Inject(method = "func_175258_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSpectator;drawTexturedModalRect(FFIIII)V", ordinal = 0))
    private void set(CallbackInfo ci) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(Hotbar.hud.position.getX(), Hotbar.hud.position.getY(), 0f);
        GlStateManager.scale(Hotbar.hud.getScale(), Hotbar.hud.getScale(), 1f);
    }

    @Inject(method = "func_175258_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"))
    private void pop(CallbackInfo ci) {
        GlStateManager.popMatrix();
    }

    @Redirect(method = "func_175258_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSpectator;func_175266_a(IIFFLnet/minecraft/client/gui/spectator/ISpectatorMenuObject;)V"))
    private void icon(GuiSpectator instance, int i, int j, float f, float g, ISpectatorMenuObject iSpectatorMenuObject) {
        GuiSpectatorAccessor accessor = (GuiSpectatorAccessor) instance;
        accessor.drawItem(i, i * 20 + 3, 3f, g, iSpectatorMenuObject);
    }

}
