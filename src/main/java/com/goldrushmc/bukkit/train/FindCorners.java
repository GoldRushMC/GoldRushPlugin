package com.goldrushmc.bukkit.train;

import org.bukkit.Location;

public class FindCorners {

    Location northWest;
    Location northEast;
    Location southWest;
    Location southEast;

    Location mostSouthernOne;
    Location mostSouthernTwo;

    Location mostNorthernOne;
    Location mostNorthernTwo;

    Location mostEasternOne;
    Location mostEasternTwo;

    Location mostWesternOne;
    Location mostWesternTwo;

    public Location getNorthWest() {
        return northWest;
    }

    public Location getNorthEast() {
        return northEast;
    }

    public Location getSouthWest() {
        return southWest;
    }

    public Location getSouthEast() {
        return southEast;
    }

    public FindCorners(Location locOne, Location locTwo, Location locThree, Location locFour) {

        getBothSouthern(locOne, locTwo, locThree, locFour);
        getBothNorthern(locOne, locTwo, locThree, locFour);
        getBothWestern(locOne, locTwo, locThree, locFour);
        getBothEastern(locOne, locTwo, locThree, locFour);


        if (mostNorthernOne == mostWesternOne) {
            northWest = mostNorthernOne;
        } else if (mostNorthernOne == mostWesternTwo) {
            northWest = mostNorthernOne;
        } else if (mostNorthernTwo == mostWesternTwo) {
            northWest = mostNorthernTwo;
        } else if (mostNorthernTwo == mostWesternTwo) {
            northWest = mostNorthernTwo;
        }

        if (mostNorthernOne == mostEasternOne) {
            northEast = mostNorthernOne;
        } else if (mostNorthernOne == mostEasternTwo) {
            northEast = mostNorthernOne;
        } else if (mostNorthernTwo == mostEasternTwo) {
            northEast = mostNorthernTwo;
        } else if (mostNorthernTwo == mostEasternTwo) {
            northEast = mostNorthernTwo;
        }

        if (mostSouthernOne == mostWesternOne) {
            southWest = mostSouthernOne;
        } else if (mostSouthernOne == mostWesternTwo) {
            southWest = mostSouthernOne;
        } else if (mostSouthernTwo == mostWesternTwo) {
            southWest = mostSouthernTwo;
        } else if (mostSouthernTwo == mostWesternTwo) {
            southWest = mostSouthernTwo;
        }

        if (mostSouthernOne == mostEasternOne) {
            southEast = mostSouthernOne;
        } else if (mostSouthernOne == mostEasternTwo) {
            southEast = mostSouthernOne;
        } else if (mostSouthernTwo == mostEasternTwo) {
            southEast = mostSouthernTwo;
        } else if (mostSouthernTwo == mostEasternTwo) {
            southEast = mostSouthernTwo;
        }
    }

    void getBothSouthern(Location locOne, Location locTwo, Location locThree, Location locFour) {
        int mostSouthern = Integer.MIN_VALUE;
        Location first = null;
        int[] zArray = new int[]{locOne.getBlockZ(), locTwo.getBlockZ(), locThree.getBlockZ(), locFour.getBlockZ()};
        for (int i = 0; i < 4; i++) {
            if (zArray[i] > mostSouthern) {
                switch (i) {
                    case 0:
                        mostSouthernOne = locOne;
                        mostSouthern = zArray[i];
                        first = locOne;
                        break;
                    case 1:
                        mostSouthernOne = locTwo;
                        mostSouthern = zArray[i];
                        first = locTwo;
                        break;
                    case 2:
                        mostSouthernOne = locThree;
                        mostSouthern = zArray[i];
                        first = locThree;
                        break;
                    case 3:
                        mostSouthernOne = locFour;
                        mostSouthern = zArray[i];
                        first = locFour;
                        break;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            if (zArray[i] == mostSouthern) {
                if (i == 0 && locOne != first) {
                    mostSouthernTwo = locOne;
                } else if (i == 1 && locTwo != first) {
                    mostSouthernTwo = locTwo;
                } else if (i == 2 && locThree != first) {
                    mostSouthernTwo = locThree;
                } else if (i == 3 && locFour != first) {
                    mostSouthernTwo = locFour;
                }
            }
        }
    }

    void getBothNorthern(Location locOne, Location locTwo, Location locThree, Location locFour) {
        int mostNorthern = Integer.MAX_VALUE;
        Location first = null;
        int[] zArray = new int[]{locOne.getBlockZ(), locTwo.getBlockZ(), locThree.getBlockZ(), locFour.getBlockZ()};
        for (int i = 0; i < 4; i++) {
            if (zArray[i] < mostNorthern) {
                switch (i) {
                    case 0:
                        mostNorthernOne = locOne;
                        mostNorthern = zArray[i];
                        first = locOne;
                        break;
                    case 1:
                        mostNorthernOne = locTwo;
                        mostNorthern = zArray[i];
                        first = locTwo;
                        break;
                    case 2:
                        mostNorthernOne = locThree;
                        mostNorthern = zArray[i];
                        first = locThree;
                        break;
                    case 3:
                        mostNorthernOne = locFour;
                        mostNorthern = zArray[i];
                        first = locFour;
                        break;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            if (zArray[i] == mostNorthern) {
                if (i == 0 && locOne != first) {
                    mostNorthernTwo = locOne;
                } else if (i == 1 && locTwo != first) {
                    mostNorthernTwo = locTwo;
                } else if (i == 2 && locThree != first) {
                    mostNorthernTwo = locThree;
                } else if (i == 3 && locFour != first) {
                    mostNorthernTwo = locFour;
                }
            }
        }
    }

    void getBothEastern(Location locOne, Location locTwo, Location locThree, Location locFour) {
        int mostEastern = Integer.MIN_VALUE;
        Location first = null;
        int[] XArray = new int[]{locOne.getBlockX(), locTwo.getBlockX(), locThree.getBlockX(), locFour.getBlockX()};
        for (int i = 0; i < 4; i++) {
            if (XArray[i] > mostEastern) {
                switch (i) {
                    case 0:
                        mostEasternOne = locOne;
                        mostEastern = XArray[i];
                        first = locOne;
                        break;
                    case 1:
                        mostEasternOne = locTwo;
                        mostEastern = XArray[i];
                        first = locTwo;
                        break;
                    case 2:
                        mostEasternOne = locThree;
                        mostEastern = XArray[i];
                        first = locThree;
                        break;
                    case 3:
                        mostEasternOne = locFour;
                        mostEastern = XArray[i];
                        first = locFour;
                        break;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            if (XArray[i] == mostEastern) {
                if (i == 0 && locOne != first) {
                    mostEasternTwo = locOne;
                } else if (i == 1 && locTwo != first) {
                    mostEasternTwo = locTwo;
                } else if (i == 2 && locThree != first) {
                    mostEasternTwo = locThree;
                } else if (i == 3 && locFour != first) {
                    mostEasternTwo = locFour;
                }
            }
        }
    }

    void getBothWestern(Location locOne, Location locTwo, Location locThree, Location locFour) {
        int mostWestern = Integer.MAX_VALUE;
        Location first = null;
        int[] XArray = new int[]{locOne.getBlockX(), locTwo.getBlockX(), locThree.getBlockX(), locFour.getBlockX()};
        for (int i = 0; i < 4; i++) {
            if (XArray[i] < mostWestern) {
                switch (i) {
                    case 0:
                        mostWesternOne = locOne;
                        mostWestern = XArray[i];
                        first = locOne;
                        break;
                    case 1:
                        mostWesternOne = locTwo;
                        mostWestern = XArray[i];
                        first = locTwo;
                        break;
                    case 2:
                        mostWesternOne = locThree;
                        mostWestern = XArray[i];
                        first = locThree;
                        break;
                    case 3:
                        mostWesternOne = locFour;
                        mostWestern = XArray[i];
                        first = locFour;
                        break;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            if (XArray[i] == mostWestern) {
                if (i == 0 && locOne != first) {
                    mostWesternTwo = locOne;
                } else if (i == 1 && locTwo != first) {
                    mostWesternTwo = locTwo;
                } else if (i == 2 && locThree != first) {
                    mostWesternTwo = locThree;
                } else if (i == 3 && locFour != first) {
                    mostWesternTwo = locFour;
                }
            }
        }
    }
}