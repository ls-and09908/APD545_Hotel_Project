package ca.senecacollege.hotel.utilities;

import com.google.inject.Inject;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static Properties config = new Properties();

    @Inject
    public AppConfig(){
        try(InputStream in = AppConfig.class
                .getClassLoader()
                .getResourceAsStream("config.properties")){
            if(in!=null){
                config.load(in);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static double getWeekendMultiplier(){
        return Double.parseDouble(config.getProperty("weekend_pricing_rate"));
    }

    public static double getPeakMultiplier(){
        return Double.parseDouble(config.getProperty("peak_pricing_rate"));
    }

    public static double getAdminDiscount(){
        return Double.parseDouble(config.getProperty("admin_discount_max"));
    }

    public static double getManagerDiscount(){
        return Double.parseDouble(config.getProperty("manager_discount_max"));
    }

    public static int getLoyaltyEarnRate(){
        return Integer.parseInt(config.getProperty("points_earned_per_dollar"));
    }

    public static int getLoyaltyRedemptionCap(){
        return Integer.parseInt(config.getProperty("redemption_cap"));
    }

    public static int getLoyaltyConversionRate(){
        return Integer.parseInt(config.getProperty("points_to_dollar"));
    }

    public static double getDepositPercent(){ return Double.parseDouble(config.getProperty("deposit_min_percent")); }
}
