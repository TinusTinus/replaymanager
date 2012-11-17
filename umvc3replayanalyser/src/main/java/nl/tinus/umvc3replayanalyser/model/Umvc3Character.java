package nl.tinus.umvc3replayanalyser.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Representation of a character in the video game Ultimate Marvel vs Capcom 3.
 * 
 * Named Umvc3Character instead of Character to avoid name clashes with java.lang.Character.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Umvc3Character {
    AKUMA("Akuma", "Akuma", "Gohadoken", "Tatsumaki Zankukyaku", "Hyakki Gojin"),
    AMATERASU("Amaterasu", "Ammy", "Solar Flare", "Cold Star", "Bloom"),
    ARTHUR("Arthur", "Arthur", "Heavenly Slash", "Dagger Toss", "Fire Bottle Toss"),
    CAPTAIN_AMERICA("Captain America", "Cap", "Shield Slash", "Stars & Stripes", "Charging Star"),
    C_VIPER("C. Viper", "Viper", "Thunder Knuckle", "Seismic Hammer", "Burning Kick"),
    CHRIS("Chris", "Chris", "Combination Punch", "Gun Fire Machine Gun", "Grenade Toss"),
    CHUN_LI("Chun-Li", "Chun", "Kikoken", "Tenshokyaku", "Hyakuretsukyaku"),
    DANTE("Dante", "Dante", "Jam Session", "Crystal", "Weasel Shot"),
    DEADPOOL("Deadpool", "Deadpool", "Quick Work", "Katana-Rama", "Trigger Happy"),
    DOCTOR_DOOM("Doctor Doom", "Doom", "Plasma Beam", "Hidden Missiles", "Molecular Shield"),
    DOCTOR_STRANGE("Doctor Strange", "Strange", "Daggers of Denak", "Eye of Agamotto", "Bolts of Balthakk"),
    DORMAMMU("Dormammu", "Dorm", "Dark Hole", "Purification", "Liberation"),
    FELICIA("Felicia", "Felicia", "Rolling Buckler ~ Rolling Slide", "Sand Splash", "Cat Spike"),
    FIREBRAND("Firebrand", "Firebrand", "Hell Spitfire", "Demon Missile M", "Demon Missilie H"),
    FRANK_WEST("Frank West", "Frank", "Shopping Cart", "Tools of Survival", "Pick Me Up"),
    GHOST_RIDER("Ghost Rider", "GhostRider", "Chain of Rebuttal", "Heartless Spire", "Hellfire"),
    HAGGAR("Haggar", "Haggar", "Double Lariat", "Violent Ax", "Steel Pipe"),
    HAWKEYE("Hawkeye", "Hawkeye", "Quick Shot Greyhound", "Trick Shot Violet Fuzz", "Ragtime Shot Kamikaze"),
    HSIEN_KO("Hsien-Ko", "HsienKo", "Senbu Pu", "Henkyo Ki", "Anki Hou"),
    HULK("Hulk", "Hulk", "Gamma Wave", "Anti-Air Gamma Charge", "Gamme Charge"),
    IRON_FIST("Iron Fist", "IronFist", "Dragon's Touch", "Crescent Heal", "Rising Fan"),
    IRON_MAN("Iron Man", "IronMan", "Unibeam", "Repulsor Blast", "Smart Bomb"),
    JILL("Jill", "Jill", "Flip Kick", "Arrow Kick", "Somersault Kick"),
    MAGNETO("Magneto", "Magneto", "Electromagnetic Disruptor", "Hyper Gravitation", "Force Field"),
    MODOK("M.O.D.O.K.", "MODOK", "Barrier", "Balloon Bomb", "Psionic Blast"),
    MORRIGAN("Morrigan", "Morrigan", "Shadow Blade", "Soul Fist", "Dark Harmonizer"),
    NEMESIS("Nemesis T-Type", "Nemesis", "Clothesline Rocket", "Launcher Slam", "Rocket Launcher"),
    // TODO check Nova's assists in-game; strategy guide and Shoryuken contradict
    NOVA("Nova", "Nova", "Gravimetric Pulse", "Centurion Rush", "Nova Strike"),
    PHOENIX("Phoenix", "Phoenix", "TK Shot", "TK Overdrive", "TK Trap"),
    PHOENIX_WRIGHT("Phoenix Wright", "Wright", "Paperwork High", "Press the Witness", "Get 'Em, Missile"),
    ROCKET_RACCOON("Rocket Raccoon", "Raccoon", "Spitfire + Spitfire Twice", "Claymore", "Pendulum"),
    RYU("Ryu", "Ryu", "Shoryuken", "Hadoken", "Tasumaki Senpukyaku"),
    SENTINEL("Sentinel", "Sent", "Sentinel Force Charge", "Sentinel Force Bomb", "Rocket Punch"),
    SHE_HULK("She-Hulk", "Shulk", "Torpedo", "Clothesline", "Somersault Kick"),
    SHUMA_GORATH("Shuma-Gorath", "Shuma", "Mystic Ray", "Mystic Stare", "Mystic Smash"),
    SPENCER("Spencer", "Spencer", "Wire Grapple H Shot", "Wire Grapple Slant Shot", "Armor Piercer"),
    SPIDER_MAN("Spider-Man", "Spidey", "Web Ball", "Web Swing", "Spider Sting"),
    STORM("Storm", "Storm", "Whirlwind", "Double Typhoon", "Lightning Attack"),
    STRIDER_HIRYU("Strider Hiryu", "Strider", "Ame-no-Murakumo", "Gram", "Vajra"),
    SUPER_SKRULL("Super-Skrull", "Skrull", "Stone Smite", "Orbital Grudge", "Tenderizer"),
    TASKMASTER("Taskmaster", "Task", "Aim Master Horizontal Shot", "Aim Master Parabolic Shot", "Aim Master Vertical Shot"),
    THOR("Thor", "Thor", "Mighty Spark", "Mighty Smash", "Mighty Strike"),
    TRISH("Trish", "Trish", "Trick \"Hopscotch\"", "Trick \"Peekaboo\"", "Low Voltage"),
    TRON("Tron", "Tron", "Bonne Strike", "Gustaff Fire", "Bandit Boulder"),
    VERGIL("Vergil", "Vergil", "Judgement Cut", "Rising Sun", "Rapid Slash"),
    VIEWTIFUL_JOE("Viewtiful Joe", "Joe", "Voomerang", "Groovy Uppercut", "Shocking Pink"),
    WESKER("Wesker", "Wesker", "Ghost Butterfly", "Samurai Edge Lower Shot", "Jaguar Dash"),
    WOLVERINE("Wolverine", "Wolvie", "Tornado Claw", "Berserker Slash", "Berserker Barrage"),
    X_23("X-23", "X23", "Neck Slice", "Ankle Slice", "Crescent Scythe"),
    ZERO("Zero", "Zero", "Ryuenjin", "Hadengeki", "Shippuga");
    
    /** The character's name, as it appears in-game. */
    private final String name;
    /** A shortened version of the character's name, containing no spaces, dashes or other special characters. */
    private final String shortName;
    /** Name of the character's first assist move. */
    private final String alphaAssistName;
    /** Name of the character's second assist move. */
    private final String betaAssistName;
    /** Name of the character's third assist move. */
    private final String gammaAssistName;
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return name;
    }
    
    /** 
     * Returns the name of the assist move of the given type for this character
     * 
     * @return move name
      */
    public String getAssistName(AssistType type) {
        String result;
        if (type == AssistType.ALPHA) {
            result = this.alphaAssistName;
        } else if (type == AssistType.BETA) {
            result = this.betaAssistName;
        } else if (type == AssistType.GAMMA) {
            result = this.gammaAssistName;
        } else {
            throw new IllegalArgumentException("Invalid assist type: " + type);
        }
        return result;
    }
}
