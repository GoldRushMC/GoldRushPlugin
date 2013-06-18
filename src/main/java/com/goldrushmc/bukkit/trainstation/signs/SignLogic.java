package com.goldrushmc.bukkit.trainstation.signs;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignLogic implements ISignLogic {

    private Map<String, Sign> signs = new HashMap<>();
    private Map<Sign, SignType> signTypes = new HashMap<>();
    private List<Sign> signList = new ArrayList<>();

    public SignLogic(List<Block> blocks) {
        findRelevantSigns(blocks);
    }


    @Override
    public List<Sign> getSigns() {
        return signList;
    }

    @Override
    public void addSign(Sign sign, SignType type) {
        this.signTypes.put(sign, type);
    }

    @Override
    public boolean addSign(String signName, Sign sign) {
        String type = sign.getLine(1);
        SignType lookFor = SignType.valueOf(type);
        //If the sign is not of a type we care about, reject the process.
        if (lookFor == null) return false;

        //If it is a type we need, add it to the lists.
        switch (lookFor) {
            case FIX_BRIDGE:
            case TRAIN_STATION_DEPARTING:
            case TRAIN_STATION_CART_COUNT:
            case TRAIN_STATION_DIRECTION:
            case TRAIN_STATION_TIME:
            case TRAIN_STATION_PASSENGER_EXIT: {
                this.signTypes.put(sign, lookFor);
                this.signList.add(sign);
                this.signs.put(signName, sign);
                return true;
            }
            default:
                break;
        }

        return false;

    }

    @Override
    public void removeSign(String signName) {
        if (this.signs.containsKey(signName)) {
            Sign sign = this.signs.get(signName);
            if (this.signTypes.containsKey(sign)) this.signTypes.remove(sign);
        }
    }

    @Override
    public void findRelevantSigns(List<Block> blocks) {
        for (Block b : blocks) {
            if (b.getType().equals(Material.SIGN) || b.getType().equals(Material.WALL_SIGN) || b.getType().equals(Material.SIGN_POST)) {
                Sign s = (Sign) b.getState();
                String[] lines = s.getLines();
                //Make sure it has a length of 4. It always should...
                if (lines.length == 4) {
                    if (lines[0].equals("{trains}")) {
                        //We can remove the {trains} thing, it is unnecessary at this point.
                        s.setLine(0, "");
                        switch (lines[1]) {
                            case "Departing":
                                signTypes.put(s, SignType.TRAIN_STATION_DEPARTING);
                                break;
                            case "Time Remaining":
                                signTypes.put(s, SignType.TRAIN_STATION_TIME);
                                break;
                            case "Carts Available":
                                signTypes.put(s, SignType.TRAIN_STATION_CART_COUNT);
                                break;
                            case "Passenger Exit":
                                signTypes.put(s, SignType.TRAIN_STATION_PASSENGER_EXIT);
                                break;
                        }
                        if (signTypes.containsKey(s)) {
                            signList.add(s);
                            signs.put(lines[1], s);
                        }
                        //Update the sign so that the {trains} gets removed.
                        s.update();
                    }
//                    //TODO Could be used for house signs.
//                 else if(lines[0].equals("{houses}")) {
//
//                    }
//                    //TODO Could be used for bank signs.
//                 else if(lines[0].equals("{banks}")) {
//
//                    }
                }
            }
        }
    }


    @Override
    public Sign getSign(String signName) {
        return this.signs.get(signName);
    }

    @Override
    public SignType getSignType(Sign sign) {
        return this.signTypes.get(sign);
    }


    @Override
    public Map<Sign, SignType> getSignTypes() {
        return signTypes;
    }

    @Override
    public List<Sign> getSigns(SignType type) {
        List<Sign> signsToReturn = new ArrayList<>();

        for (Sign sign : this.signTypes.keySet()) {
            if (this.signTypes.get(sign).equals(type)) {
                signsToReturn.add(sign);
            }
        }
        return signsToReturn;
    }

    @Override
    public void updateTrain(String trainName) {
        List<Sign> signsToChange = new ArrayList<>();
        //Get the signs to change.
        for (Sign sign : signTypes.keySet()) {
            SignType s = signTypes.get(sign);
            switch (s) {
                case FIX_BRIDGE:
                    break;
                case TRAIN_STATION_DEPARTING:
                    sign.setLine(2, trainName);
                    break;
                case TRAIN_STATION_CART_COUNT:
                case TRAIN_STATION_TIME:
                    sign.setLine(2, "N/A");
                    break;
                default:
                    break;
            }
        }
    }
}