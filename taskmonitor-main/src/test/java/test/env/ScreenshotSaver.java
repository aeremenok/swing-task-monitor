/**
 * 
 */
package test.env;

import java.awt.Component;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.fest.swing.image.ScreenshotTaker;

/**
 * @author aeremenok 2010
 */
public class ScreenshotSaver
{
    private final ScreenshotTaker  screenshotTaker = new ScreenshotTaker();
    private final SimpleDateFormat format          = new SimpleDateFormat( "yyyy-dd-MM_hh-mm-ss" );

    public void saveDesktopScreenshot( final String screenshotDir )
    {
        new File( screenshotDir ).mkdir();
        screenshotTaker.saveDesktopAsPng( screenshotDir + "/" + newFileName() );
    }

    public void saveScreenshot( final Component component, final String screenshotDir )
    {
        new File( screenshotDir ).mkdir();
        screenshotTaker.saveComponentAsPng( component, screenshotDir + "/" + newFileName() );
    }

    protected String newFileName()
    {
        return "scr" + format.format( new Date() ) + ".png";
    }
}
