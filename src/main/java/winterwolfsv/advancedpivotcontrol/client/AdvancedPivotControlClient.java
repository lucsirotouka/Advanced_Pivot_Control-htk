package winterwolfsv.advancedpivotcontrol.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigManager;
import net.minecraft.network.chat.Component;
import winterwolfsv.config.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;

import static winterwolfsv.advancedpivotcontrol.client.Commands.sendCommandFeedback;

@Environment(EnvType.CLIENT)
public class AdvancedPivotControlClient implements ClientModInitializer {
    public static ConfigManager configManager;
    public static final String MOD_ID = "advanced_pivot_control";
    public static float currentYaw;
    public static float currentPitch;

    private static final KeyMapping yawRight = KeyMappingHelper.registerKeyMapping(new KeyMapping("Turn right", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT, KeyMapping.Category.MISC));
    private static final KeyMapping yawLeft = KeyMappingHelper.registerKeyMapping(new KeyMapping("Turn left", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT, KeyMapping.Category.MISC));
    private static final KeyMapping pitchUp = KeyMappingHelper.registerKeyMapping(new KeyMapping("Look up", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UP, KeyMapping.Category.MISC));
    private static final KeyMapping pitchDown = KeyMappingHelper.registerKeyMapping(new KeyMapping("Look down", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_DOWN, KeyMapping.Category.MISC));
    private static final KeyMapping lockYaw = KeyMappingHelper.registerKeyMapping(new KeyMapping("Lock yaw", InputConstants.Type.KEYSYM, -1, KeyMapping.Category.MISC));
    private static final KeyMapping lockPitch = KeyMappingHelper.registerKeyMapping(new KeyMapping("Lock pitch", InputConstants.Type.KEYSYM, -1, KeyMapping.Category.MISC));

    private static int turnYaw(int direction, int degrees) {
        Player player = Minecraft.getInstance().player;
        assert player != null;
        currentYaw = roundToClosestValue(direction, degrees, player.getYRot()) % 360;
        if (currentYaw > 180) currentYaw -= 360;
        else if (currentYaw < -180) currentYaw += 360;
        player.setYRot(currentYaw);
        return (int) currentYaw;
    }

    private static float roundToClosestValue(int direction, int degrees, float value) {
        if (value % degrees == 0) return value + direction * degrees;
        if (direction < 0) return (int) Math.floor(value / degrees) * degrees;
        if (direction > 0) return (int) Math.ceil(value / degrees) * degrees;
        return value;
    }

    private static int turnPitch(int direction, int degrees) {
        Player player = Minecraft.getInstance().player;
        assert player != null;
        currentPitch = roundToClosestValue(direction, degrees, player.getXRot());
        player.setXRot(currentPitch);
        return (int) Math.min(90, Math.max(-90, currentPitch));
    }

    @Override
    public void onInitializeClient() {
        configManager = (ConfigManager) AutoConfig.register(Config.class, GsonConfigSerializer::new);
        Config config = AutoConfig.getConfigHolder(Config.class).getConfig();
        lockView();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (yawRight.consumeClick()) {
                sendCommandFeedback(Component.translatable(MOD_ID + ".keybinding.turn_right", config.yawSteps, turnYaw(1, config.yawSteps)));
            }
            while (yawLeft.consumeClick()) {
                sendCommandFeedback(Component.translatable(MOD_ID + ".keybinding.turn_left", config.yawSteps, turnYaw(-1, config.yawSteps)));
            }
            while (pitchUp.consumeClick()) {
                sendCommandFeedback(Component.translatable(MOD_ID + ".keybinding.turn_up", config.pitchSteps, turnPitch(-1, config.pitchSteps)));
            }
            while (pitchDown.consumeClick()) {
                sendCommandFeedback(Component.translatable(MOD_ID + ".keybinding.turn_down", config.pitchSteps, turnPitch(1, config.pitchSteps)));
            }
            while (lockYaw.consumeClick()) {
                config.lockYaw = !config.lockYaw;
                configManager.save();
                sendCommandFeedback(Component.translatable(MOD_ID + ".keybinding.yaw_lock", config.lockYaw ? "enabled" : "disabled"));
            }
            while (lockPitch.consumeClick()) {
                config.lockPitch = !config.lockPitch;
                configManager.save();
                sendCommandFeedback(Component.translatable(MOD_ID + ".keybinding.pitch_lock", config.lockPitch ? "enabled" : "disabled"));
            }
        });
    }

    private void lockView() {
        Config config = AutoConfig.getConfigHolder(Config.class).getConfig();
        AtomicBoolean oldLockYaw = new AtomicBoolean(config.lockYaw);
        AtomicBoolean oldLockPitch = new AtomicBoolean(config.lockPitch);
        ClientTickEvents.END_LEVEL_TICK.register(client -> {
            Player player = Minecraft.getInstance().player;
            if (oldLockYaw.get() != config.lockYaw || oldLockPitch.get() != config.lockPitch) {
                if (player == null) return;
                currentYaw = player.getYRot();
                currentPitch = player.getXRot();
            }
            if (config.lockYaw && player != null && player.getYRot() != currentYaw) {
                player.setYRot(currentYaw);
            }
            if (config.lockPitch && player != null && player.getXRot() != currentPitch) {
                player.setXRot(currentPitch);
            }
            oldLockPitch.set(config.lockPitch);
            oldLockYaw.set(config.lockYaw);
        });
    }
}
