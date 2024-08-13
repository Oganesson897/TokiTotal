package me.oganesson.tokitotal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.apache.commons.codec.language.bm.Languages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.management.ManagementFactory;

@Mixin(value = FMLClientHandler.class, remap = false)
public abstract class MixinFMLClientHandler {

    @Shadow public abstract Minecraft getClient();

    @Unique
    private static boolean tokiTotal$start = true;

    @Inject(method = "finishMinecraftLoading", at = @At("RETURN"))
    private void inject$tokitotal$loadingTime(CallbackInfo ci) {
        if (!tokiTotal$start) {
            return;
        }
        long timeMillis = ManagementFactory.getRuntimeMXBean().getUptime();

        String titleRaw = "游戏启动耗时：%.1f 秒";
        String contentRaw = "已打败全世界 99.99% 的玩家";

        if (getClient().getLanguageManager().getCurrentLanguage().getLanguageCode().equals("en_us")) {
            titleRaw = "Game startup took %.1f seconds";
            contentRaw = "Beat 99.99% of the world's players.";
        }
        TextComponentString title = new TextComponentString(String.format(titleRaw, timeMillis/1000.0));
        TextComponentString content = new TextComponentString(contentRaw);
        SystemToast.addOrUpdate(getClient().getToastGui(), SystemToast.Type.NARRATOR_TOGGLE, title, content);
        tokiTotal$start = false;
    }

}
