package com.deadvikingstudios.norsetown.model;

public enum EnumTileShape
{
    FULL_CUBE,//all faces full

    HALF_CUBE_NORTH, //north face full
    HALF_CUBE_EAST, //east face full
    HALF_CUBE_SOUTH, //south face full
    HALF_CUBE_WEST, //west face full
    HALF_CUBE_TOP, //top face full
    HALF_CUBE_BOT, //bot face full

    /*QUART_CUBE_NORTH_EAST_TOP, //no full faces
    QUART_CUBE_NORTH_EAST_BOT, //no full faces
    QUART_CUBE_NORTH_WEST_TOP, //no full faces
    QUART_CUBE_NORTH_WEST_BOT, //no full faces
    QUART_CUBE_SOUTH_EAST_TOP, //no full faces
    QUART_CUBE_SOUTH_EAST_BOT, //no full faces
    QUART_CUBE_SOUTH_WEST_TOP, //no full faces
    QUART_CUBE_SOUTH_WEST_BOT, //no full faces*/

    COL_THICK, //top and bot faces bigger than med and thin
    COL_MED, //top and bot faces bigger than thin
    COL_THIN, //no bigger face

    /*HALF_COL_THICK_TOP, //top faces bigger than med and thin
    HALF_COL_THICK_BOT, //bot faces bigger than med and thin
    HALF_COL_MED_TOP, //top faces bigger than thin
    HALF_COL_MED_BOT, //bot faces bigger than thin
    HALF_COL_THIN_TOP, //no bigger face
    HALF_COL_THIN_BOT, //no bigger face*/

    CROSS, //faces of 1 crossing //not standard render
    CROSS_FULL,
    CROSS_EXTENDED, //faces of 1.5 crossing //not standard render
    CUBE_CROSS_EXTENDED, //full cube and faces of 1.5 crossing // all faces full

    NULL;

    public boolean coversFullFace(EnumTileFace face)
    {
        if(this == FULL_CUBE || this == CUBE_CROSS_EXTENDED)
        {
            return true;
        }
        else if(face == EnumTileFace.NORTH && this == HALF_CUBE_NORTH)
        {
            return true;
        }
        else if(face == EnumTileFace.EAST && this == HALF_CUBE_EAST)
        {
            return true;
        }
        else if(face == EnumTileFace.SOUTH && this == HALF_CUBE_SOUTH)
        {
            return true;
        }
        else if(face == EnumTileFace.WEST && this == HALF_CUBE_WEST)
        {
            return true;
        }
        else if(face == EnumTileFace.TOP && this == HALF_CUBE_TOP)
        {
            return true;
        }
        else if(face == EnumTileFace.BOTTOM && this == HALF_CUBE_BOT)
        {
            return true;
        }
        return false;
    }

    public boolean isCenteredColumn()
    {
        switch (this)
        {
            case COL_THICK:
            case COL_MED:
            case COL_THIN:
            //case HALF_COL_THICK_TOP:
            //case HALF_COL_THICK_BOT:
            //case HALF_COL_MED_TOP:
            //case HALF_COL_MED_BOT:
            //case HALF_COL_THIN_TOP:
            //case HALF_COL_THIN_BOT:

                return true;
            default:
                return false;
        }
    }

    public boolean isOnBottom()
    {
        if(this.coversFullFace(EnumTileFace.BOTTOM))
        {
            return true;
        }
        switch (this)
        {
            case COL_THICK:
            case COL_MED:
            case COL_THIN:
            /*//case HALF_COL_THICK_TOP:
            case HALF_COL_THICK_BOT:
            //case HALF_COL_MED_TOP:
            case HALF_COL_MED_BOT:
            //case HALF_COL_THIN_TOP:
            case HALF_COL_THIN_BOT:
            //case QUART_CUBE_NORTH_EAST_TOP:
            case QUART_CUBE_NORTH_EAST_BOT:
            //case QUART_CUBE_NORTH_WEST_TOP:
            case QUART_CUBE_NORTH_WEST_BOT:
            //case QUART_CUBE_SOUTH_EAST_TOP:
            case QUART_CUBE_SOUTH_EAST_BOT:
            //case QUART_CUBE_SOUTH_WEST_TOP:
            case QUART_CUBE_SOUTH_WEST_BOT:*/

                return true;
            default:
                return false;
        }
    }

    public boolean isOnTop()
    {
        if(this.coversFullFace(EnumTileFace.TOP))
        {
            return true;
        }
        switch (this)
        {
            case COL_THICK:
            case COL_MED:
            case COL_THIN:
            /*case HALF_COL_THICK_TOP:
            //case HALF_COL_THICK_BOT:
            case HALF_COL_MED_TOP:
            //case HALF_COL_MED_BOT:
            case HALF_COL_THIN_TOP:
            //case HALF_COL_THIN_BOT:
            case QUART_CUBE_NORTH_EAST_TOP:
            //case QUART_CUBE_NORTH_EAST_BOT:
            case QUART_CUBE_NORTH_WEST_TOP:
            //case QUART_CUBE_NORTH_WEST_BOT:
            case QUART_CUBE_SOUTH_EAST_TOP:
            //case QUART_CUBE_SOUTH_EAST_BOT:
            case QUART_CUBE_SOUTH_WEST_TOP:
            //case QUART_CUBE_SOUTH_WEST_BOT:*/

                return true;
            default:
                return false;
        }
    }

    public boolean isCuboid()
    {
        return this != NULL && this != CROSS && this != CROSS_EXTENDED && this != CROSS_FULL;
    }

    public boolean isCross()
    {
        return this == CROSS || this == CROSS_EXTENDED || this == CUBE_CROSS_EXTENDED || this == CROSS_FULL;
    }

    public boolean isHalfCube()
    {
        switch (this)
        {
            case HALF_CUBE_NORTH:
            case HALF_CUBE_EAST:
            case HALF_CUBE_SOUTH:
            case HALF_CUBE_WEST:
            case HALF_CUBE_TOP:
            case HALF_CUBE_BOT:
                return true;
        }
        return false;
    }

    /*public float getThickness()
    {
        if(this.coversFullFace(EnumTileFace.BOTTOM) || this.coversFullFace(EnumTileFace.TOP))
        {
            return 1f;
        }
        if(this.coversFullFace(EnumTileFace.NORTH) || this.coversFullFace(EnumTileFace.EAST)
                || this.coversFullFace(EnumTileFace.SOUTH) || this.coversFullFace(EnumTileFace.WEST))
        {
            return 0.5f;
        }
        if(this.isQuarterTile())
        {
            return 0.25f; //it's half thickness in any direction, quarter volume
        }
        if(this.isCenteredColumn())
        {
            switch (this)
            {
                case COL_THICK:
                case HALF_COL_THICK_TOP:
                case HALF_COL_THICK_BOT:
                    return 0.75f;
                case COL_MED:
                case HALF_COL_MED_TOP:
                case HALF_COL_MED_BOT:
                    return 0.5f;
                case COL_THIN:
                case HALF_COL_THIN_TOP:
                case HALF_COL_THIN_BOT:
                    return 0.25f;
            }
        }
        return 0.0f;
    }*/

    /*public boolean isHalfColum()
    {
        if(this.isCenteredColumn())
        {
            switch (this)
            {
                case HALF_COL_THIN_BOT:
                case HALF_COL_THIN_TOP:
                case HALF_COL_MED_BOT:
                case HALF_COL_MED_TOP:
                case HALF_COL_THICK_BOT:
                case HALF_COL_THICK_TOP:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }*/


    /*public boolean isQuarterTile()
    {
        switch (this)
        {
            case QUART_CUBE_NORTH_EAST_TOP:
            case QUART_CUBE_NORTH_EAST_BOT:
            case QUART_CUBE_NORTH_WEST_TOP:
            case QUART_CUBE_NORTH_WEST_BOT:
            case QUART_CUBE_SOUTH_EAST_TOP:
            case QUART_CUBE_SOUTH_EAST_BOT:
            case QUART_CUBE_SOUTH_WEST_TOP:
            case QUART_CUBE_SOUTH_WEST_BOT:
                return true;
            default:
                return false;
        }
    }*/

    public boolean isOnFace(EnumTileFace face)
    {
        if(this == NULL)
        {
            return false;
        }
        //full cubes and half cubes
        if(this.coversFullFace(face))
        {
            return true;
        }
        //crosses that arent full cubes
        if(this.isCross() && !this.isCuboid())
        {
            return false;
        }
        switch (face)
        {
            case NORTH:
            {
                if(this.isHalfCube() && this != HALF_CUBE_WEST)
                {
                    return true;
                }
                break;
            }
            case EAST:
            {
                if(this.isHalfCube() && this != HALF_CUBE_SOUTH)
                {
                    return true;
                }
                break;
            }
            case SOUTH:
            {
                if(this.isHalfCube() && this != HALF_CUBE_NORTH)
                {
                    return true;
                }
                break;
            }
            case WEST:
            {
                if(this.isHalfCube() && this != HALF_CUBE_EAST)
                {
                    return true;
                }
                break;
            }
            case TOP:
            {
                if(this.isHalfCube() && this != HALF_CUBE_BOT)
                {
                    return true;
                }
                if(this.isCenteredColumn())
                {
                    return true;
                }

                break;
            }
            case BOTTOM:
            {
                if(this.isHalfCube() && this != HALF_CUBE_TOP)
                {
                    return true;
                }
                if(this.isCenteredColumn())
                {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public boolean renderThisFace(EnumTileFace thisFace, EnumTileShape otherTile)
    {
        //if it's not on the face, render it
        if(this.isOnFace(thisFace) && otherTile.coversFullFace(thisFace.getOpposite()))
        {
            return false;
        }
        if(this == COL_THIN && (otherTile == COL_THIN || otherTile == COL_MED || otherTile == COL_THICK))
        {
            return false;
        }
        if(this == COL_MED && (otherTile == COL_MED || otherTile == COL_THICK))
        {
            return false;
        }
        if(this == COL_THICK && otherTile == COL_THICK)
        {
            return false;
        }
        //if this shares the face with a full cube
        if(this.isOnFace(thisFace) && (otherTile == FULL_CUBE || otherTile == CUBE_CROSS_EXTENDED))
        {
            return false;
        }



        return true;

    }

    /*public boolean isOtherFaceGTThisFace(EnumTileShape otherTile, EnumTileFace thisFace, EnumTileFace otherFace)
    {
        if(otherTile == EnumTileShape.NULL) return false;
        if(otherTile.coversFullFace(thisFace) && this.coversFullFace(otherFace))
        {
            return false; //faces are equal
        }
        else if(otherTile.coversFullFace(otherFace))
        {
            return true; //other face is full, this face isnt
        }
        else if(this.coversFullFace(thisFace))
        {
            return false; //this face is full, other face isnt
        }

        if(this.isCenteredColumn())
        {
            if (otherFace == EnumTileFace.TOP && thisFace == EnumTileFace.TOP)
            {
                if(this.isOnTop() && otherTile.isOnTop())
                {
                    return this.getThickness() < otherTile.getThickness();
                }
            } else if (otherFace == EnumTileFace.TOP && thisFace == EnumTileFace.BOTTOM)
            {
                if(this.isOnTop() && otherTile.isOnBottom())
                {
                    return this.getThickness() < otherTile.getThickness();
                }
            } else if (otherFace == EnumTileFace.BOTTOM && thisFace == EnumTileFace.TOP)
            {
                if(this.isOnBottom() && otherTile.isOnTop())
                {
                    return this.getThickness() < otherTile.getThickness();
                }
            } else if (otherFace == EnumTileFace.BOTTOM && thisFace == EnumTileFace.BOTTOM)
            {
                if(this.isOnBottom() && otherTile.isOnBottom())
                {
                    return this.getThickness() < otherTile.getThickness();
                }
            }
        }
        //TODO add clause for quarter tiles that share a face
        if(this.isQuarterTile() && otherTile.isQuarterTile())
        {
            //check if they share 2 faces and 1 opposing face
        }
        return false;
    }
    public boolean isOtherFaceGTEQThisFace(EnumTileShape otherTile, EnumTileFace thisFace, EnumTileFace otherFace)
    {
        if(otherTile == EnumTileShape.NULL) return false;
        if(otherTile.isCross()) return false;
        if(otherTile.coversFullFace(thisFace) && this.coversFullFace(otherFace))
        {
            return true; //faces are equal
        }
        else if(otherTile.coversFullFace(otherFace))
        {
            return true; //other face is full, this face isnt
        }
        else if(this.coversFullFace(thisFace))
        {
            return false; //this face is full, other face isnt
        }

        if(this.isCenteredColumn())
        {
            if (otherFace == EnumTileFace.TOP && thisFace == EnumTileFace.TOP)
            {
                if(this.isOnTop() && otherTile.isOnTop())
                {
                    return this.getThickness() <= otherTile.getThickness();
                }
            } else if (otherFace == EnumTileFace.TOP && thisFace == EnumTileFace.BOTTOM)
            {
                if(this.isOnTop() && otherTile.isOnBottom())
                {
                    return this.getThickness() <= otherTile.getThickness();
                }
            } else if (otherFace == EnumTileFace.BOTTOM && thisFace == EnumTileFace.TOP)
            {
                if(this.isOnBottom() && otherTile.isOnTop())
                {
                    return this.getThickness() <= otherTile.getThickness();
                }
            } else if (otherFace == EnumTileFace.BOTTOM && thisFace == EnumTileFace.BOTTOM)
            {
                if(this.isOnBottom() && otherTile.isOnBottom())
                {
                    return this.getThickness() <= otherTile.getThickness();
                }
            }
            return true;
        }
        //TODO add clause for quarter tiles that share a face
        return false;
    }*/
}
