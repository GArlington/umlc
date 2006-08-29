package tools;


public class Debug {
    
    private static int debug_level;
        
    public static void setDebug_level(int _level) {
        debug_level = _level;
    }


    public static int getDebug_level() {
	return debug_level;
    }
    
    
    /* Method only prints debug message if debug level is greater than
     * or equal to the level provided
     */
    
    public static void print(int level, String message) {
        if (debug_level >= level) {
            System.out.print(message);
        }
    }
   
    public static void println(int level, String message) {
        if (debug_level >= level) {
            System.out.println(message);
        }
    }
    
    
}
