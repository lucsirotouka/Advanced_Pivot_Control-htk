package winterwolfsv.advancedpivotcontrol.client;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import winterwolfsv.config.Config;

import static winterwolfsv.advancedpivotcontrol.client.AdvancedPivotControlClient.MOD_ID;

public class Commands {

    public static void register() {
        final String[] nameList = new String[]{"advancedpivotcontrol", "apc"};

        for (int i = 0; i < nameList.length; i++) {
            int finalI = i;

            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommands.literal(nameList[finalI])
                    .then(ClientCommands.literal("pitch")
                            .then(ClientCommands.argument("pitch", DoubleArgumentType.doubleArg(-90, 90))
                                    .executes(context -> commandSetPitch((float) DoubleArgumentType.getDouble(context, "pitch")))))

                    .then(ClientCommands.literal("yaw")
                            .then(ClientCommands.argument("yaw", DoubleArgumentType.doubleArg(-180, 180))
                                    .executes(context -> commandSetYaw((float) DoubleArgumentType.getDouble(context, "yaw")))))


                    .then(ClientCommands.literal("angle")
                            .then(ClientCommands.argument("pitch", DoubleArgumentType.doubleArg(-90, 90))
                                    .then(ClientCommands.argument("yaw", DoubleArgumentType.doubleArg(-180, 180))
                                            .executes(context -> commandSetAngle((float) DoubleArgumentType.getDouble(context, "pitch"), (float) DoubleArgumentType.getDouble(context, "yaw"))))))
                    .then(ClientCommands.literal("config")
                            .then(ClientCommands.literal("setpitchsteps")
                                    .then(ClientCommands.argument("pitch", DoubleArgumentType.doubleArg(1, 90))
                                            .executes(context -> commandSetPitchSteps((int) DoubleArgumentType.getDouble(context, "pitch"))))
                                    .executes(context -> {
                                        context.getSource().sendFeedback(Component.literal("Pitch Steps is set to: " + AutoConfig.getConfigHolder(Config.class).getConfig().pitchSteps));
                                        return 1;
                                    }))
                            .then(ClientCommands.literal("setyawsteps")
                                    .then(ClientCommands.argument("yaw", DoubleArgumentType.doubleArg(1, 180))
                                            .executes(context -> commandSetYawSteps((int) DoubleArgumentType.getDouble(context, "yaw"))))
                                    .executes(context -> {
                                        context.getSource().sendFeedback(Component.literal("Yaw Steps is set to: " + AutoConfig.getConfigHolder(Config.class).getConfig().yawSteps));
                                        return 1;
                                    }))

                            .then(ClientCommands.literal("docommandfeedback")
                                    .then(ClientCommands.argument("doCommandFeedback", BoolArgumentType.bool())
                                            .executes(context -> commandDoCommandFeedback(BoolArgumentType.getBool(context, "doCommandFeedback"))))
                                    .executes(context -> {
                                        context.getSource().sendFeedback(Component.literal("Do Command Feedback is set to: " + AutoConfig.getConfigHolder(Config.class).getConfig().messageFeedback));
                                        return 1;
                                    }))
                            .then(ClientCommands.literal("lockyaw")
                                    .then(ClientCommands.argument("lockYaw", BoolArgumentType.bool())
                                            .executes(context -> commandLockYaw(BoolArgumentType.getBool(context, "lockYaw"))))
                                    .executes(context -> {
                                        context.getSource().sendFeedback(Component.literal("Yaw Lock is set to: " + AutoConfig.getConfigHolder(Config.class).getConfig().lockYaw));
                                        return 1;
                                    }))
                            .then(ClientCommands.literal("lockPitch")
                                    .then(ClientCommands.argument("lockPitch", BoolArgumentType.bool())
                                            .executes(context -> commandLockPitch(BoolArgumentType.getBool(context, "lockPitch"))))
                                    .executes(context -> {
                                        context.getSource().sendFeedback(Component.literal("Pitch Lock is set to: " + AutoConfig.getConfigHolder(Config.class).getConfig().lockPitch));
                                        return 1;
                                    })))));


            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommands.literal("pitch")
                    .then(ClientCommands.argument("pitch", DoubleArgumentType.doubleArg(-90, 90))
                            .executes(context -> commandSetPitch((float) DoubleArgumentType.getDouble(context, "pitch"))))));

            ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommands.literal("yaw")
                    .then(ClientCommands.argument("yaw", DoubleArgumentType.doubleArg(-180, 180))
                            .executes(context -> commandSetYaw((float) DoubleArgumentType.getDouble(context, "yaw")))))));

            ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommands.literal("lockyaw")
                    .then(ClientCommands.argument("lockYaw", BoolArgumentType.bool())
                            .executes(context -> commandLockYaw(BoolArgumentType.getBool(context, "lockYaw"))))
                    .executes(context -> {
                        context.getSource().sendFeedback(Component.literal("Lock Yaw is set to: " + AutoConfig.getConfigHolder(Config.class).getConfig().lockYaw));
                        return 1;
                    }))));

            ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommands.literal("lockpitch")
                    .then(ClientCommands.argument("lockPitch", BoolArgumentType.bool())
                            .executes(context -> commandLockPitch(BoolArgumentType.getBool(context, "lockPitch"))))
                    .executes(context -> {
                        context.getSource().sendFeedback(Component.literal("Lock Pitch is set to: " + AutoConfig.getConfigHolder(Config.class).getConfig().lockPitch));
                        return 1;
                    }))));

            ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommands.literal("lockangle")
                    .then(ClientCommands.argument("lockAngle", BoolArgumentType.bool())
                            .executes(context -> commandLockAngle(BoolArgumentType.getBool(context, "lockAngle"))))
                    .executes(context -> {
                        boolean lockAngle = AutoConfig.getConfigHolder(Config.class).getConfig().lockYaw && AutoConfig.getConfigHolder(Config.class).getConfig().lockPitch;
                        context.getSource().sendFeedback(Component.literal("Lock Angle is set to: " + lockAngle));
                        return 1;
                    }))));

            ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommands.literal("angle")
                    .then(ClientCommands.argument("yaw", DoubleArgumentType.doubleArg(-180, 180))
                            .then(ClientCommands.argument("pitch", DoubleArgumentType.doubleArg(-90, 90))
                                    .executes(context -> commandSetAngle((float) DoubleArgumentType.getDouble(context, "pitch"), (float) DoubleArgumentType.getDouble(context, "yaw"))))))));

        }
    }

    private static int commandDoCommandFeedback(boolean doCommandFeedback) {
        AutoConfig.getConfigHolder(Config.class).getConfig().messageFeedback = doCommandFeedback;
        AutoConfig.getConfigHolder(Config.class).save();
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.sendOverlayMessage(Component.translatable(MOD_ID + ".command.doCommandFeedback", doCommandFeedback));
        } else {
            System.out.println(Component.translatable(MOD_ID + ".command.doCommandFeedback", doCommandFeedback));
        }
        return 1;
    }

    private static int commandSetPitch(float value) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            System.out.println(Component.translatable(MOD_ID + ".command.player_null"));
            return 0;
        }
        player.setXRot(value);
        AdvancedPivotControlClient.currentPitch = value;
        sendCommandFeedback(Component.translatable(MOD_ID + ".command.pitch_set", value));
        return 1;

    }

    private static int commandSetPitchSteps(int value) {
        AutoConfig.getConfigHolder(Config.class).getConfig().pitchSteps = value;
        AutoConfig.getConfigHolder(Config.class).save();
        sendCommandFeedback(Component.translatable(MOD_ID + ".command.pitch_steps_set", value));
        return 1;
    }

    private static int commandSetYaw(float value) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            System.out.println(Component.translatable(MOD_ID + ".command.player_null"));
            return 0;
        }
        player.setYRot(value);
        AdvancedPivotControlClient.currentYaw = value;
        sendCommandFeedback(Component.translatable(MOD_ID + ".command.yaw_set", value));
        return 1;

    }

    private static int commandSetYawSteps(int value) {
        AutoConfig.getConfigHolder(Config.class).getConfig().yawSteps = value;
        AutoConfig.getConfigHolder(Config.class).save();
        sendCommandFeedback(Component.translatable(MOD_ID + ".command.yaw_steps_set", value));
        return 1;
    }

    private static int commandSetAngle(float pitch, float yaw) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            System.out.println(Component.translatable(MOD_ID + ".command.player_null"));
            return 0;
        }
        player.setXRot(pitch);
        player.setYRot(yaw);
        AdvancedPivotControlClient.currentPitch = pitch;
        AdvancedPivotControlClient.currentYaw = yaw;
        sendCommandFeedback(Component.translatable(MOD_ID + ".command.angle_set", pitch, yaw));
        return 1;

    }

    private static int commandLockYaw(boolean lockYaw) {
        AutoConfig.getConfigHolder(Config.class).getConfig().lockYaw = lockYaw;
        AutoConfig.getConfigHolder(Config.class).save();
        sendCommandFeedback(Component.translatable(MOD_ID + ".command.yaw_lock", AutoConfig.getConfigHolder(Config.class).getConfig().lockYaw));
        return 1;
    }

    private static int commandLockPitch(boolean lockPitch) {
        AutoConfig.getConfigHolder(Config.class).getConfig().lockPitch = lockPitch;
        AutoConfig.getConfigHolder(Config.class).save();
        sendCommandFeedback(Component.translatable(MOD_ID + ".command.pitch_lock", AutoConfig.getConfigHolder(Config.class).getConfig().lockPitch));
        return 1;
    }

    private static int commandLockAngle(boolean lockAngle) {
        AutoConfig.getConfigHolder(Config.class).getConfig().lockYaw = lockAngle;
        AutoConfig.getConfigHolder(Config.class).getConfig().lockPitch = lockAngle;
        AutoConfig.getConfigHolder(Config.class).save();
        sendCommandFeedback(Component.translatable(MOD_ID + ".command.angle_lock", lockAngle));
        return 1;
    }

    public static void sendCommandFeedback(String message) {
        if (!AutoConfig.getConfigHolder(Config.class).getConfig().messageFeedback) return;
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            System.out.println(message);
            return;
        }
        player.sendOverlayMessage(Component.literal(message));

    }

    public static void sendCommandFeedback(Component message) {
        if (!AutoConfig.getConfigHolder(Config.class).getConfig().messageFeedback) return;
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            System.out.println(message);
            return;
        }
        player.sendOverlayMessage(message);

    }
}
