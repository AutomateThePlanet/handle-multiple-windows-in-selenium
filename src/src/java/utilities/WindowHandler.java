package utilities;

import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

//    Supplier       ()    -> x
//    Consumer       x     -> ()
//    BiConsumer     x, y  -> ()
//    Callable       ()    -> x throws ex
//    Runnable       ()    -> ()
//    Function       x     -> y
//    BiFunction     x,y   -> z
//    Predicate      x     -> boolean
//    UnaryOperator  x1    -> x2
//    BinaryOperator x1,x2 -> x3
public class WindowHandler {
    public static void handle(WebDriver driver, String windowTitle, Boolean shouldClose, Runnable action) {
        List<String> allWindows = driver.getWindowHandles().stream().toList();
        String mainWindow = driver.getWindowHandle();
        for (var currentWindow:allWindows) {
            if (!mainWindow.equalsIgnoreCase(currentWindow)) {
                var childWindow = driver.switchTo().window(currentWindow);
                childWindow.manage().window().maximize();
                if(driver.getTitle().contains(windowTitle)){
                    action.run();
                }

                if (shouldClose) {
                    driver.close();
                }

                driver.switchTo().window(mainWindow);
            }
        }
    }
}
