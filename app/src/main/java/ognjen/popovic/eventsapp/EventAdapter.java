package ognjen.popovic.eventsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Event> events;

    public EventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setEvents(ArrayList<Event> newEvents) {
        events.clear();
        events.addAll(newEvents);
        notifyDataSetChanged();
    }

    public void clearEvents() {
        events.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.event_item, parent, false);

            holder = new ViewHolder();

            holder.eventItemRoot =
                    convertView.findViewById(R.id.eventItemRoot);

            holder.imgEvent =
                    convertView.findViewById(R.id.imgEvent);

            holder.tvFeatured =
                    convertView.findViewById(R.id.tvFeatured);

            holder.tvEventName =
                    convertView.findViewById(R.id.tvEventName);

            holder.tvEventCategory =
                    convertView.findViewById(R.id.tvEventCategory);

            holder.tvEventLocation =
                    convertView.findViewById(R.id.tvEventLocation);

            holder.tvEventDateTime =
                    convertView.findViewById(R.id.tvEventDateTime);

            holder.tvFreePlaces =
                    convertView.findViewById(R.id.tvFreePlaces);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Event event = events.get(position);

        holder.imgEvent.setImageResource(event.getImageResId());
        holder.tvEventName.setText(event.getName());
        holder.tvEventCategory.setText(event.getCategory());
        holder.tvEventLocation.setText(event.getLocation());
        holder.tvEventDateTime.setText(event.getDateTime());

        if (event.isPromoted()) {

            holder.tvFeatured.setVisibility(View.VISIBLE);
            holder.tvFreePlaces.setVisibility(View.VISIBLE);

            int freePlaces =
                    event.getCapacity() - event.getAttendingCount();

            holder.tvFreePlaces.setText(
                    context.getString(
                            R.string.free_places_format,
                            freePlaces,
                            event.getCapacity()
                    )
            );

            holder.eventItemRoot.setBackgroundResource(
                    R.color.light_gray
            );

        } else {

            holder.tvFeatured.setVisibility(View.GONE);
            holder.tvFreePlaces.setVisibility(View.GONE);

            holder.eventItemRoot.setBackgroundResource(
                    R.color.white
            );
        }

        return convertView;
    }

    private static class ViewHolder {

        LinearLayout eventItemRoot;
        ImageView imgEvent;
        TextView tvFeatured;
        TextView tvEventName;
        TextView tvEventCategory;
        TextView tvEventLocation;
        TextView tvEventDateTime;
        TextView tvFreePlaces;
    }
}