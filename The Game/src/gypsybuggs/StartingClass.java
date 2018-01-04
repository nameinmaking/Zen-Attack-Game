package gypsybuggs;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import gypsybuggs.framework.Animation;

public class StartingClass extends Applet implements Runnable, KeyListener{

    private static Background bg1,bg2;
    private Robo robot;
    private Zombie hb1,hb2;
    private Image image;

    // The currentSprite will contain the state of the character at that momemnt among the three
    private Image currentSprite,character1,character2,character3,character4,characterDown,characterJumped;
    private Image background;

    // Enemy bots
    private Image zombie1,zombie2,zombie3,zombie4;
    private Graphics second;
    private URL base;

    //Animation objects
    // cAnim = character
    // zAnim = zombie
    private Animation cAnim,zAnim;


    public static Image tilegrassTop,tilegrassBot,tilegrassLeft,tilegrassRight,tileDirt;

    private ArrayList<Tile> tileArray = new ArrayList<Tile>();


    // Initialize the whole scene
    @Override
    public void init() {
        setSize(800,480);
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        Frame frame = (Frame) this.getParent().getParent();
        frame.setTitle("The Rouge Bot");

        try
        {
            base = StartingClass.class.getResource("/data");

        }catch (Exception ex){
            ex.printStackTrace();
        }

        // Image Setups
        character1 = getImage(base,"data/character1.png");
        character2 = getImage(base,"data/character2.png");
        character3 = getImage(base,"data/character3.png");
        character4 = getImage(base,"data/character4.png");
        characterDown = getImage(base,"data/down.png");
        characterJumped = getImage(base,"data/jumped.png");

        zombie1 = getImage(base, "data/zombie1.png");
        zombie2 = getImage(base, "data/zombie2.png");
        zombie3 = getImage(base, "data/zombie3.png");
        zombie4 = getImage(base, "data/zombie4.png");

        background = getImage(base,"data/background.png");
        tileDirt = getImage(base,"data/tiledirt.png");
        tilegrassTop = getImage(base,"data/tilegrasstop.png");
        tilegrassBot = getImage(base,"data/tilegrassbot.png");
        tilegrassLeft = getImage(base,"data/tilegrassleft.png");
        tilegrassRight = getImage(base,"data/tilegrassright.png");

        cAnim = new Animation();
        cAnim.addFrame(character1,80);
        cAnim.addFrame(character2,80);
        cAnim.addFrame(character3,80);
        cAnim.addFrame(character4,80);
        cAnim.addFrame(character3,80);
        cAnim.addFrame(character2,80);

        zAnim = new Animation();
        zAnim.addFrame(zombie1,1000);
        zAnim.addFrame(zombie2,1000);
        zAnim.addFrame(zombie3,1000);
        zAnim.addFrame(zombie4,1000);
        zAnim.addFrame(zombie3,1000);
        zAnim.addFrame(zombie2,1000);

        currentSprite = cAnim.getImage();
    }

    @Override
    public void start()
    {
        int i,j;

        // Background
        bg1 = new Background(0,0);
        bg2 = new Background(2160,0);

        //Tiles
        // Total possible locations: 200x12 = 2400
        // Filling tiles in: 200x2 = 400
//        for (i=0;i<200;i++)
//        {
//            for (j=0;j<12;j++)
//            {
//                if (j==11)
//                {
//                    Tile t = new Tile(i,j,2);
//                    tileArray.add(t);
//                }
//                if (j==10)
//                {
//                    Tile t = new Tile (i,j,1);
//                    tileArray.add(t);
//                }
//            }
//        }

        //Initialize tiles
        try
        {
//            File currentDIr = new File(".");
            loadMap("data/map1.txt");
        }catch(IOException exr)
        {
            exr.printStackTrace();
        }



        //Characters
        hb1 = new Zombie(340,360);
        hb2 = new Zombie(700,360);
        robot = new Robo();

        Thread thread = new Thread(this);
        thread.start();
    }

    private void loadMap(String fileName) throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;
        int i,j;
        String line ="";
        char ch;
        Tile t;

//        BufferedReader br = new BufferedReader(new FileReader("E:\\test.txt"));
        InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));


        while(true)
        {
            line = br.readLine();

            if (line == null)
            {
                br.close();
                break;
            }

            if (!line.startsWith("!"))
            {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }
        height = lines.size();

        for (j=0;j<12;j++)
        {
            line = lines.get(j).toString();
            for (i=0;i<width;i++)
            {
                System.out.println(i + " is i ");

                if (i<line.length())
                {
                    ch = line.charAt(i);
                    t = new Tile(i,j,Character.getNumericValue(ch));
                    tileArray.add(t);
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    //    The game loop
    //    1. Update characters
    //    2. Update environment
    //    3. Repaint the scene
    //    4. Sleep for 17 (1000/60) miliseconds ~ 60 fps
    @Override
    public void run()
    {
        while(true)
        {
            robot.update();
            if (robot.isJumped())
                currentSprite = characterJumped;
            else if (robot.isJumped()==false && robot.isDucked()==false)
                currentSprite = cAnim.getImage();

            ArrayList projectiles = robot.getProjectiles();
            for (int i=0;i<projectiles.size();i++)
            {
                Projectile p = (Projectile) projectiles.get(i);
                if (p.isVisible())
                    p.update();
                else
                    projectiles.remove(i);
            }

            updateTile();

            hb1.update();
            hb2.update();
            bg1.update();
            bg2.update();

            animate();
            repaint();  //To draw every object on the scene once again

            try {
                Thread.sleep(17);
            }
            catch(InterruptedException ie)
            {
                ie.printStackTrace();
            }
        }

    }

    public void animate()
    {
        cAnim.update(10);
        zAnim.update(50);
    }

    //Implicitly called automatically and will loop over and over again
    // using double-buffering technique to give a flicker free experience
    public void update(Graphics g)
    {
        if (image==null)
        {
            image = createImage(this.getWidth(),this.getHeight());
            second = image.getGraphics();
        }

        second.setColor(getBackground());
        second.fillRect(0,0,getWidth(),getHeight());
        second.setColor(getForeground());
        paint(second);

        g.drawImage(image,0,0,this);
    }

    //always called with the statement repaint() inside the method run()
    public void paint(Graphics g)
    {
        // SYNTAX: g.drawImage(img, x, y, observer)

        // background
        g.drawImage(background,bg1.getBgX(),bg1.getBgY(),this);
        g.drawImage(background,bg2.getBgX(),bg2.getBgY(),this);
        paintTiles(g);


        // drawing bullets on the screen
        ArrayList projectiles = robot.getProjectiles();
        for (int i=0;i<projectiles.size();i++)
        {
            Projectile p = (Projectile) projectiles.get(i);
            g.setColor(Color.YELLOW);
            g.fillRect(p.getX(),p.getY(),10,5);
        }

        g.drawRect((int)robot.rect1.getX(), (int)robot.rect1.getY(), (int)robot.rect1.getWidth(), (int)robot.rect1.getHeight());
        g.drawRect((int)robot.rect2.getX(), (int)robot.rect2.getY(), (int)robot.rect2.getWidth(), (int)robot.rect2.getHeight());
        // characters on the screen
        g.drawImage(currentSprite,robot.getCenterX()-61,robot.getCenterY()-63,this);
        g.drawImage(zAnim.getImage(),hb1.getCenterX()-48,hb1.getCenterY()-48,this);
        g.drawImage(zAnim.getImage(),hb2.getCenterX()-48,hb2.getCenterY()-48,this);
    }


    private void updateTile()
    {
        int i;
        Tile t;
        for (i=0;i<tileArray.size();i++)
        {
            t = (Tile) tileArray.get(i);
            t.update();
        }
    }

    private void paintTiles(Graphics g)
    {
        int i;
        Tile t;

        // lopping from 0 to 2399
        for (i=0;i<tileArray.size();i++)
        {
            t = (Tile) tileArray.get(i);
            g.drawImage(t.getTileImage(),t.getTileX(),t.getTileY(),this);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                System.out.println("Move UP");
                break;
            case KeyEvent.VK_DOWN:
                currentSprite = characterDown;
                if (robot.isJumped() == false)
                {
                    robot.setDucked(true);
                    robot.setSpeedX(0);
                }
                break;
            case KeyEvent.VK_LEFT:
                robot.moveLeft();
                robot.setMovingLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                robot.moveRight();
                robot.setMovingRight(true);
                break;
            case KeyEvent.VK_SPACE:
                robot.jump();

                break;
            case KeyEvent.VK_CONTROL:
                if (!robot.isDucked()&&!robot.isJumped())
                    robot.shoot();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                System.out.println("Stop moving UP");
                break;
            case KeyEvent.VK_DOWN:
                currentSprite = cAnim.getImage();
                robot.setDucked(false);
                break;
            case KeyEvent.VK_LEFT:
                robot.stopLeft();
                break;
            case KeyEvent.VK_RIGHT:
                robot.stopRight();
                break;
            case KeyEvent.VK_SPACE:
                System.out.println("Stop the JUMP");
                break;
        }
    }


    public static Background getBg1() {
        return bg1;
    }

    public static Background getBg2() {
        return bg2;
    }

}
