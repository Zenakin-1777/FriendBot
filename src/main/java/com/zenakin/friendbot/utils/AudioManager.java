package com.zenakin.friendbot.utils;

import com.zenakin.friendbot.config.FriendBotConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

public class AudioManager {
/* V2 NEITHER
    public static void playPingSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(
                PositionedSoundRecord.create(new ResourceLocation("friendbot", "notification_ping"))
        );
    }
 */

/* V1 DIDN'T WORK
    private final Minecraft mc = Minecraft.getMinecraft();
    private static AudioManager instance;

    private boolean minecraftSoundEnabled = false;
    private long lastPingTime = 0;
    private int numSounds = 15;
    private float soundBeforeChange = 0;
    private static Clip clip;

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public boolean isMinecraftSoundEnabled() {
        return minecraftSoundEnabled;
    }

    public void setMinecraftSoundEnabled(boolean minecraftSoundEnabled) {
        this.minecraftSoundEnabled = minecraftSoundEnabled;
    }

    public float getSoundBeforeChange() {
        return soundBeforeChange;
    }

    public void setSoundBeforeChange(float soundBeforeChange) {
        this.soundBeforeChange = soundBeforeChange;
    }

    public void resetSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            return;
        }
        minecraftSoundEnabled = false;
        mc.gameSettings.setSoundLevel(SoundCategory.MASTER, soundBeforeChange);
    }

    public void playSound(String filePath) {
        Multithreading.schedule(() -> {
            try {
                AudioInputStream inputStream;
                File audioFile = new File(filePath);
                if (audioFile.exists() && audioFile.isFile()) {
                    inputStream = AudioSystem.getAudioInputStream(audioFile);
                } else {
                    URL resource = getClass().getResource(filePath);
                    if (resource == null) {
                        System.err.println("[Audio Manager] Failed to load sound file from path: " + filePath);
                        return;
                    }
                    inputStream = AudioSystem.getAudioInputStream(resource);
                }

                if (inputStream == null) {
                    System.err.println("[Audio Manager] Failed to load sound file!");
                    return;
                }

                clip = AudioSystem.getClip();
                clip.open(inputStream);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float volumePercentage = 1.0f; // Customize as needed
                float dB = (float) (Math.log(volumePercentage) / Math.log(10.0) * 20.0);
                volume.setValue(dB);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println(e.getMessage());
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

    public boolean isSoundPlaying() {
        return (clip != null && clip.isRunning()) || minecraftSoundEnabled;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (!minecraftSoundEnabled) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPingTime < 100) return; // Replace 100 with your desired delay in milliseconds

        if (numSounds <= 0) {
            minecraftSoundEnabled = false;
            mc.gameSettings.setSoundLevel(SoundCategory.MASTER, soundBeforeChange);
            return;
        }

        mc.theWorld.playSound(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, "friendbot:notification_ping", 10.0F, 1.0F, false);

        lastPingTime = currentTime;
        numSounds--;
    }
 */

        /*
        private static Minecraft mc = Minecraft.getMinecraft();
        private static long lastPlayed = System.currentTimeMillis();
        private static String[] defaultSounds = new String[]{"mob.blaze.hit", "fire.ignite", "random.orb", "random.break", "mob.guardian.land.hit", "note.pling", "friendbot:notification_ping"};

        public static void notificationPing() {
            if(System.currentTimeMillis() - lastPlayed <= 5){
                return;
            }
            playLoudSound("friendbot:notification_ping", FriendBotConfig.customVolume, FriendBotConfig.customPitch, mc.thePlayer.getPositionVector());
            lastPlayed = System.currentTimeMillis();
        }
         */

        public static void playLoudSound(String sound, Float volume, Float pitch, Vec3 pos) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft.getMinecraft().theWorld.playSound(pos.xCoord, pos.yCoord+1.62, pos.zCoord, sound, volume, pitch, false);
            });
        }

}