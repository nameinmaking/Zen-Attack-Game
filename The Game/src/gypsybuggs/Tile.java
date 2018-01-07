package gypsybuggs;

import java.awt.*;

public class Tile
{
    int tileX,tileY,speedX,type;
    Image tileImage;

    private Rectangle r;

    private Robo robot = StartingClass.getRobot();
    private Background bg = StartingClass.getBg1();

    public Tile(int x,int y,int tileType)
    {
        // size of each tile is 40 pixels
        tileX = x * 40;
        tileY = y * 40;
        type = tileType;
        r = new Rectangle();

        if (type == 5)
            tileImage = StartingClass.tileDirt;
        else if (type ==8)
            tileImage = StartingClass.tilegrassTop;
        else if (type ==4)
            tileImage = StartingClass.tilegrassLeft;
        else if (type ==6)
            tileImage = StartingClass.tilegrassRight;
        else if (type ==2)
            tileImage = StartingClass.tilegrassBot;
        else                //when no tile is found in the area,ie, no platform is present
            type = 0;


    }

    // adding Parallax scrolling: background images move slower than foreground
    // creating and illusion of depth in a 2D scene and adding to the immersion.
    // Using the ocean tile to put in the Parallax Effect.
    public void update()
    {
        speedX = bg.getSpeedX()*5;
        tileX += speedX;
        r.setBounds(tileX,tileY,40,40);

        if (r.intersects(Robo.yellowRed)&&type!=0)
        {
            checkVerticalCollision(Robo.rect1,Robo.rect2);
            checkSideCollision(Robo.gunRect,Robo.leftFoot,Robo.rightFoot);
        }
//        if (type == 1)
//        {
//            if (bg.getSpeedX() == 0)
//                setSpeedX(-1);
//            else
//                setSpeedX(-2);
//        }
//        else
//            setSpeedX(bg.getSpeedX()*5);
//
//        tileX += speedX;
//
//        // setting tile bounding box values
//        r.setBounds(tileX,tileY,40,40);
//
//        //call collision detection
//        if (type!= 0)
//            checkVerticalCollision(Robo.rect1, Robo.rect2);
    }

    //collision detection and action function
    public void checkVerticalCollision(Rectangle rTop,Rectangle rBottom)
    {
        if (rTop.intersects(r))
            System.out.println("Upper Collision");

        if (rBottom.intersects(r))
            System.out.println("Bottom Collision");
    }

    public void checkSideCollision(Rectangle gun,Rectangle leftF,Rectangle rightF)
    {
        if (type !=5 && type !=2 && type !=0)
        {
            if (gun.intersects(r))
            {
                robot.setCenterX(tileX + 85);
                robot.setSpeedX(0);
            }
            else if (rightF.intersects(r))
            {
                robot.setCenterX(tileX-45);
                robot.setSpeedX(0);
            }

            if (leftF.intersects(r))
            {
                robot.setCenterX(tileX + 85);
                robot.setSpeedX(0);
            }
        }
    }

    //Getters and Setters
    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Image getTileImage() {
        return tileImage;
    }

    public void setTileImage(Image tileImage) {
        this.tileImage = tileImage;
    }

    public Background getBg() {
        return bg;
    }

    public void setBg(Background bg) {
        this.bg = bg;
    }
}
