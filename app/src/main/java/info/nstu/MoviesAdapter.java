package info.nstu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import info.androidhive.recyclerview.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    ColorGenerator generator = ColorGenerator.MATERIAL;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        ImageView letter;



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            letter = (ImageView) view.findViewById(R.id.gmailitem_letter);

        }
    }


    public MoviesAdapter(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
        String letter = String.valueOf(movie.getTitle().charAt(0));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor());
        holder.letter.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
