package gypsybuggs.framework;

import java.awt.*;
import java.util.ArrayList;

public class Animation
{
    private ArrayList frames;
    private int currentFrame;        //start from 0
    private long animationTime;     //time elapsed since current image was displayed
    private long totalDuration;     //the total duration for which the image will be displayed

    public Animation()
    {
        frames = new ArrayList();
        totalDuration = 0;

        synchronized (this)
        {
            animationTime = 0;
            currentFrame = 0;
        }
    }


    // takes an AnimationFrame and appends to the ArrayList frames
    public synchronized void addFrame(Image image, long duration)
    {
        totalDuration += duration;
        frames.add(new AnimationFrame(image, totalDuration));
    }

    public synchronized  void update(long elapsedTime)
    {
        if (frames.size() > 1)
        {
            animationTime += elapsedTime;
            if (animationTime >= totalDuration)
                animationTime= animationTime%totalDuration;
            currentFrame = 0;
        }

        while (animationTime > getFrame(currentFrame).endTime)
            currentFrame++;
    }

    public synchronized Image getImage()
    {
        if (frames.size()==0)
            return null;
        else
            return getFrame(currentFrame).image;
    }

    private AnimationFrame getFrame(int i)
    {
        return  (AnimationFrame) frames.get(i);
    }

    private class AnimationFrame
    {
        Image image;
        long endTime;

        public AnimationFrame(Image image,long endTime)
        {
            this.image = image;
            this.endTime = endTime;
        }
    }

    public ArrayList getFrames() {
        return frames;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public long getAnimationTime() {
        return animationTime;
    }

    public long getTotalDuration() {
        return totalDuration;
    }
}
