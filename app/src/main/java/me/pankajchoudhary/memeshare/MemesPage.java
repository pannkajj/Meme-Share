package me.pankajchoudhary.memeshare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;

public class MemesPage extends AppCompatActivity {

    String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memes_page);

        Intent i = getIntent();
        this.source = i.getStringExtra("source");

        showMeme();
    }

    private void showMeme() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final ProgressBar progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);


        final ImageView memeImageView = findViewById(R.id.memeImageView);

        memeImageView.setVisibility(View.GONE);
        String url="https://meme-api.herokuapp.com/gimme/"+this.source;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            String currentImageUrl = null;
            try {
                currentImageUrl = response.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Glide.with(MemesPage.this).load(currentImageUrl).into(memeImageView);
            progressBar.setVisibility(View.GONE);
            memeImageView.setVisibility(View.VISIBLE);
        }, error -> Toast.makeText(MemesPage.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show());

        queue.add(jsonObjectRequest);
    }

    public void shareMeme(View view) {
        ImageView imageView = findViewById(R.id.memeImageView);
        Bitmap image = getBitmapFromView(imageView);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/");
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(this, image));
        startActivity(Intent.createChooser(share, "Share Via"));
    }

    private Bitmap getBitmapFromView(ImageView imageView) {
        Bitmap returnedBitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        imageView.draw(canvas);
        return returnedBitmap;
    }

    public void nextMeme(View view) {
        showMeme();
    }

    private Uri getImageUri(Context inContext, Bitmap inImage){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}