package fields;

import javax.security.auth.login.AppConfigurationEntry;
import java.lang.reflect.Field;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        printDeclaredFields(Movie.class);
//
//        printDeclaredFields(Movie.MovieStats.class);
//
//        printDeclaredFields(Category.class);

        Movie movie = new Movie("LOTR ", 2001, 11.99, true, Category.ADVENTURE);
        printDeclaredFields2(movie.getClass(), movie);

        Field minPriceStaticField = Movie.class.getDeclaredField("MINIMUM_PRICE");
        System.out.println(minPriceStaticField.get(null));
    }
    public static <T> void printDeclaredFields2(Class<? extends T> clazz, T instance) {
        for(Field field : clazz.getDeclaredFields()) {
            System.out.println(String.format("Field name : %s, type: %s", field.getName(), field.getType().getName()));
            System.out.println(String.format("Is synthetic? %s ", field.isSynthetic()));
            System.out.println();
        }
    }

    public static void printDeclaredFields(Class<?> clazz) {
        for(Field field : clazz.getDeclaredFields()) {
            System.out.println(String.format("Field name : %s, type: %s", field.getName(), field.getType().getName()));
            System.out.println(String.format("Is synthetic? %s ", field.isSynthetic()));
            System.out.println();
        }
    }

    public enum Category {
        ADVENTURE,
        ACTION,
        COMEDY
    }

    public static class Movie extends Product {
        public static final double MINIMUM_PRICE = 10.99;

        private boolean isReleased;
        private Category category;
        private double actualPrice;

        public Movie(String name, int year, double price, boolean isReleased, Category category) {
            super(name, year);
            this.isReleased = isReleased;
            this.category = category;
            this.actualPrice = Math.max(price, MINIMUM_PRICE);
        }

        // Nested class
        public class MovieStats {
            private double timesWatched;

            public MovieStats(double timesWatched) {
                this.timesWatched = timesWatched;
            }

            public double getRevenue() {
                return timesWatched * actualPrice;
            }
        }
    }

    // Superclass
    public static class Product {
        protected String name;
        protected int year;
        protected double actualPrice;

        public Product(String name, int year) {
            this.name = name;
            this.year = year;
        }
    }
}
