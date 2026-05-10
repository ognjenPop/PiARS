package ognjen.popovic.eventsapp;

public class EventFactory {

    // Kreiranje regularnog događaja
    public static Event createRegularEvent(
            String name,
            String description,
            String location,
            String dateTime,
            String category,
            int imageResId) {

        return new Event(
                name,
                description,
                location,
                dateTime,
                category,
                imageResId
        );
    }

    // Kreiranje promoted događaja
    public static Event createPromotedEvent(
            String name,
            String description,
            String location,
            String dateTime,
            String category,
            int imageResId,
            int capacity) {

        return new Event(
                name,
                description,
                location,
                dateTime,
                category,
                imageResId,
                true,
                capacity,
                0
        );
    }
}
