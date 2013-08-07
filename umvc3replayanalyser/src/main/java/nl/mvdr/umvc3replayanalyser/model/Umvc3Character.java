/*
 * Copyright 2012, 2013 Martijn van de Rijdt 
 * 
 * This file is part of the Ultimate Marvel vs Capcom 3 Replay Manager.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with the Ultimate Marvel vs Capcom 3
 * Replay Manager. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mvdr.umvc3replayanalyser.model;

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
    /** Akuma, also known as Gouki. */
    AKUMA("Akuma", "Akuma", "Gohadoken", "Tatsumaki Zankukyaku", "Hyakki Gojin"),
    /** Amaterasu. */
    AMATERASU("Amaterasu", "Ammy", "Solar Flare", "Cold Star", "Bloom"),
    /** Arthur. */
    ARTHUR("Arthur", "Arthur", "Heavenly Slash", "Dagger Toss", "Fire Bottle Toss"),
    /** Catain America. */
    CAPTAIN_AMERICA("Captain America", "Cap", "Shield Slash", "Stars & Stripes", "Charging Star"),
    /** Crimson Viper. */
    C_VIPER("C. Viper", "Viper", "Thunder Knuckle", "Seismic Hammer", "Burning Kick"),
    /** Chris Redfield. */
    CHRIS("Chris", "Chris", "Combination Punch", "Gun Fire Machine Gun", "Grenade Toss"),
    /** Chun-Li. */
    CHUN_LI("Chun-Li", "Chun", "Kikoken", "Tenshokyaku", "Hyakuretsukyaku"),
    /** Dante, Son of Sparda. */
    DANTE("Dante", "Dante", "Jam Session", "Crystal", "Weasel Shot"),
    /** Deadpool. */
    DEADPOOL("Deadpool", "Deadpool", "Quick Work", "Katana-Rama", "Trigger Happy"),
    /** Doctor Victor von Doom. */
    DOCTOR_DOOM("Doctor Doom", "Doom", "Plasma Beam", "Hidden Missiles", "Molecular Shield"),
    /** Doctor Strange. */
    DOCTOR_STRANGE("Doctor Strange", "Strange", "Daggers of Denak", "Eye of Agamotto", "Bolts of Balthakk"),
    /** Dormammu. */
    DORMAMMU("Dormammu", "Dorm", "Dark Hole", "Purification", "Liberation"),
    /** Felicia. */
    FELICIA("Felicia", "Felicia", "Rolling Buckler ~ Rolling Slide", "Sand Splash", "Cat Spike"),
    /** Firebrand. */
    FIREBRAND("Firebrand", "Firebrand", "Hell Spitfire", "Demon Missile M", "Demon Missilie H"),
    /** Frank West. */
    FRANK_WEST("Frank West", "Frank", "Shopping Cart", "Tools of Survival", "Pick Me Up"),
    /** Ghost Rider. */
    GHOST_RIDER("Ghost Rider", "GhostRider", "Chain of Rebuttal", "Heartless Spire", "Hellfire"),
    /** Mike Haggar. */
    HAGGAR("Haggar", "Haggar", "Double Lariat", "Violent Ax", "Steel Pipe"),
    /** Hawkeye. */
    HAWKEYE("Hawkeye", "Hawkeye", "Quick Shot Greyhound", "Trick Shot Violet Fuzz", "Ragtime Shot Kamikaze"),
    /** Hsien-Ko, also known as Lei-Lei. */
    HSIEN_KO("Hsien-Ko", "HsienKo", "Senbu Pu", "Henkyo Ki", "Anki Hou"),
    /** The Incredible Hulk. */
    HULK("Hulk", "Hulk", "Gamma Wave", "Anti-Air Gamma Charge", "Gamma Charge"),
    /** Iron Fist. */
    IRON_FIST("Iron Fist", "IronFist", "Dragon's Touch", "Crescent Heal", "Rising Fan"),
    /** Iron Man. */
    IRON_MAN("Iron Man", "IronMan", "Unibeam", "Repulsor Blast", "Smart Bomb"),
    /** Jill Valentine. */
    JILL("Jill", "Jill", "Flip Kick", "Arrow Kick", "Somersault Kick"),
    /** Magneto. */
    MAGNETO("Magneto", "Magneto", "Electromagnetic Disruptor", "Hyper Gravitation", "Force Field"),
    /** M.O.D.O.K.. */
    MODOK("M.O.D.O.K.", "MODOK", "Barrier", "Balloon Bomb", "Psionic Blast"),
    /** Morrigan Aensland. */
    MORRIGAN("Morrigan", "Morrigan", "Shadow Blade", "Soul Fist", "Dark Harmonizer"),
    /** Nemesis. */
    NEMESIS("Nemesis T-Type", "Nemesis", "Clothesline Rocket", "Launcher Slam", "Rocket Launcher"),
    /** Nova. */
    NOVA("Nova", "Nova", "Gravimetric Pulse", "Centurion Rush", "Nova Strike"),
    /** Phoenix, also known as Jean Grey. */
    PHOENIX("Phoenix", "Phoenix", "TK Shot", "TK Overdrive", "TK Trap"),
    /** Phoenix Wright, also known as Naruhodou Ryuuichi. */
    PHOENIX_WRIGHT("Phoenix Wright", "Wright", "Paperwork High", "Press the Witness", "Get 'Em, Missile"),
    /** Rocket Raccoon. */
    ROCKET_RACCOON("Rocket Raccoon", "Raccoon", "Spitfire + Spitfire Twice", "Claymore", "Pendulum"),
    /** Ryu. */
    RYU("Ryu", "Ryu", "Shoryuken", "Hadoken", "Tasumaki Senpukyaku"),
    /** Sentinel. */
    SENTINEL("Sentinel", "Sent", "Sentinel Force Charge", "Sentinel Force Bomb", "Rocket Punch"),
    /** She-Hulk. */
    SHE_HULK("She-Hulk", "Shulk", "Torpedo", "Clothesline", "Somersault Kick"),
    /** Shuma-Gorath. */
    SHUMA_GORATH("Shuma-Gorath", "Shuma", "Mystic Ray", "Mystic Stare", "Mystic Smash"),
    /** Nathan Spencer. */
    SPENCER("Spencer", "Spencer", "Wire Grapple H Shot", "Wire Grapple Slant Shot", "Armor Piercer"),
    /** Spider-Man. */
    SPIDER_MAN("Spider-Man", "Spidey", "Web Ball", "Web Swing", "Spider Sting"),
    /** Storm. */
    STORM("Storm", "Storm", "Whirlwind", "Double Typhoon", "Lightning Attack"),
    /** Strider Hiryu. */
    STRIDER_HIRYU("Strider Hiryu", "Strider", "Ame-no-Murakumo", "Gram", "Vajra"),
    /** Super-Skrull. */
    SUPER_SKRULL("Super-Skrull", "Skrull", "Stone Smite", "Orbital Grudge", "Tenderizer"),
    /** Taskmaster. */
    TASKMASTER("Taskmaster", "Task", "Aim Master Horizontal Shot", "Aim Master Parabolic Shot", "Aim Master Vertical Shot"),
    /** Thor Odinson. */
    THOR("Thor", "Thor", "Mighty Spark", "Mighty Smash", "Mighty Strike"),
    /** Trish. */
    TRISH("Trish", "Trish", "Trick \"Hopscotch\"", "Trick \"Peekaboo\"", "Low Voltage"),
    /** Tron Bonne. */
    TRON("Tron", "Tron", "Bonne Strike", "Gustaff Fire", "Bandit Boulder"),
    /** Vergil, Son of Sparda. */
    VERGIL("Vergil", "Vergil", "Judgement Cut", "Rising Sun", "Rapid Slash"),
    /** Viewtiful Joe. */
    VIEWTIFUL_JOE("Viewtiful Joe", "Joe", "Voomerang", "Groovy Uppercut", "Shocking Pink"),
    /** Albert Wesker. */
    WESKER("Wesker", "Wesker", "Ghost Butterfly", "Samurai Edge Lower Shot", "Jaguar Dash"),
    /** Wolverine. */
    WOLVERINE("Wolverine", "Wolvie", "Tornado Claw", "Berserker Slash", "Berserker Barrage"),
    /** X-23. */
    X_23("X-23", "X23", "Neck Slice", "Ankle Slice", "Crescent Scythe"),
    /** Zero. */
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
