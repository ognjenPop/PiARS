package ognjen.popovic.eventsapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AppData {

    public static ArrayList<Event> allEvents = new ArrayList<>();

    public static ArrayList<Event> interestedEvents =
            new ArrayList<>();

    public static ArrayList<Event> attendingEvents =
            new ArrayList<>();

    static {

        // PARTY

        allEvents.add(EventFactory.createRegularEvent(
                "Rooftop Party",
                "Summer rooftop party",
                "Belgrade",
                "20.07.2026 21:00",
                "Party",
                R.drawable.party1
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Beach Party",
                "Night beach event",
                "Budva",
                "15.08.2026 22:00",
                "Party",
                R.drawable.party2
        ));

        // FESTIVAL

        allEvents.add(EventFactory.createPromotedEvent(
                "EXIT Festival",
                "Biggest music festival",
                "Novi Sad",
                "10.07.2026 18:00",
                "Festival",
                R.drawable.exit,
                50000
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Beer Fest",
                "Beer and music festival",
                "Belgrade",
                "25.08.2026 17:00",
                "Festival",
                R.drawable.festival1
        ));

        // CONCERT

        allEvents.add(EventFactory.createRegularEvent(
                "Rock Concert",
                "Live rock concert",
                "Nis",
                "12.06.2026 20:00",
                "Concert",
                R.drawable.concert1
        ));

        allEvents.add(EventFactory.createPromotedEvent(
                "Drake Live",
                "World tour concert",
                "Budapest",
                "05.09.2026 20:00",
                "Concert",
                R.drawable.concert2,
                70000
        ));

        // STAND-UP & THEATER

        allEvents.add(EventFactory.createRegularEvent(
                "Stand-Up Night",
                "Comedy evening",
                "Novi Sad",
                "18.05.2026 19:00",
                "Stand-Up & Theater",
                R.drawable.theater1
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Hamlet",
                "Theater performance",
                "Belgrade",
                "22.06.2026 20:00",
                "Stand-Up & Theater",
                R.drawable.theater2
        ));

        // EXHIBITION

        allEvents.add(EventFactory.createRegularEvent(
                "Art Expo",
                "Modern art exhibition",
                "Novi Sad",
                "10.06.2026 12:00",
                "Exhibition",
                R.drawable.exhibition1
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Photography Expo",
                "Photography exhibition",
                "Belgrade",
                "15.07.2026 14:00",
                "Exhibition",
                R.drawable.exhibition2
        ));

        // PROŠLI DOGAĐAJI

        allEvents.add(EventFactory.createRegularEvent(
                "Old Concert",
                "Past concert",
                "Belgrade",
                "10.01.2024 20:00",
                "Concert",
                R.drawable.concert1
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Winter Party",
                "Past party",
                "Kopaonik",
                "01.02.2024 21:00",
                "Party",
                R.drawable.party1
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Old Festival",
                "Past festival",
                "Novi Sad",
                "15.03.2024 18:00",
                "Festival",
                R.drawable.festival1
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Old Theater",
                "Past theater event",
                "Nis",
                "05.04.2024 19:00",
                "Stand-Up & Theater",
                R.drawable.theater1
        ));

        // DODATNI EVENTI

        allEvents.add(EventFactory.createRegularEvent(
                "Jazz Night",
                "Jazz concert",
                "Belgrade",
                "15.09.2026 20:00",
                "Concert",
                R.drawable.concert2
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Tech Expo",
                "Technology exhibition",
                "Novi Sad",
                "11.10.2026 11:00",
                "Exhibition",
                R.drawable.exhibition1
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Foam Party",
                "Foam night party",
                "Budva",
                "22.07.2026 23:00",
                "Party",
                R.drawable.party2
        ));

        allEvents.add(EventFactory.createRegularEvent(
                "Summer Festival",
                "Outdoor festival",
                "Zlatibor",
                "12.08.2026 18:00",
                "Festival",
                R.drawable.festival1
        ));

        // INTERESTED EVENTS

        interestedEvents.add(allEvents.get(0));
        interestedEvents.add(allEvents.get(2));
        interestedEvents.add(allEvents.get(4));
        interestedEvents.add(allEvents.get(6));
        interestedEvents.add(allEvents.get(8));

        // ATTENDING EVENTS

        attendingEvents.add(allEvents.get(1));
        attendingEvents.add(allEvents.get(2));
        attendingEvents.add(allEvents.get(3));
        attendingEvents.add(allEvents.get(5));
        attendingEvents.add(allEvents.get(10));
        attendingEvents.add(allEvents.get(11));
        attendingEvents.add(allEvents.get(12));
        attendingEvents.add(allEvents.get(13));
    }

    public static ArrayList<Event> getSortedEvents() {

        ArrayList<Event> sortedList =
                new ArrayList<>(allEvents);

        Collections.sort(sortedList,
                (e1, e2) ->
                        Boolean.compare(
                                e2.isPromoted(),
                                e1.isPromoted()));

        return sortedList;
    }

    public static ArrayList<Event>
    getSortedEventsByCategory(String category) {

        ArrayList<Event> filteredList =
                new ArrayList<>();

        for (Event event : allEvents) {

            if (event.getCategory().equals(category)) {
                filteredList.add(event);
            }
        }

        Collections.sort(filteredList,
                (e1, e2) ->
                        Boolean.compare(
                                e2.isPromoted(),
                                e1.isPromoted()));

        return filteredList;
    }

    public static Event findByName(String name) {

        for (Event event : allEvents) {

            if (event.getName().equals(name)) {
                return event;
            }
        }

        return null;
    }
}