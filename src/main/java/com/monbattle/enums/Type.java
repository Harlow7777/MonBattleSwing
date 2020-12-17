package com.monbattle.enums;

public enum Type {
    WATER,
    ELECTRIC,
    NATURE,
    GROUND,
    AIR,
    FIRE,
    MIND,
    METAL,
    GHOST,
    MUSCLE,
    SOUND,
    SPACE;

    private static final double[][] interactionTable =
    {
                    //water elect nture ground air  fire  mind  metal ghost mscle sound space
       /* water */ {   1.0,  1.0,  0.5,  2.0,  1.0,  2.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0   },
       /* elect */ {   2.0,  1.0,  1.0,  0.5,  2.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5   },
       /* nture */ {   2.0,  1.0,  1.0,  2.0,  1.0,  1.0,  0.5,  1.0,  1.0,  1.0,  1.0,  1.0   },
       /* groun */ {   1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0   },
       /* air   */ {   1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  1.0,  1.0,  1.0,  2.0,  1.0,  0.5   },
       /* fire  */ {   0.5,  1.0,  2.0,  1.0,  1.0,  1.0,  1.0,  2.0,  1.0,  1.0,  1.0,  1.0   },
       /* mind  */ {   1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  2.0,  1.0,  1.0   },
       /* metal */ {   1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0   },
       /* ghost */ {   1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  2.0,  1.0,  1.0,  1.0,  1.0,  1.0   },
       /* mscle */ {   1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0   },
       /* sound */ {   1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0   },
       /* space */ {   1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0   }
    };

    public static double typeInteraction(Type t, Type t2) {
        return interactionTable[t.ordinal()][t2.ordinal()];
    }
}
