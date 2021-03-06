package io.geeteshk.hyper.activity;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;

import io.geeteshk.hyper.R;
import io.geeteshk.hyper.adapter.RemotesAdapter;
import io.geeteshk.hyper.git.Giiit;
import io.geeteshk.hyper.helper.Theme;

public class RemotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Theme.getThemeInt(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remotes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final File repo = new File(getIntent().getStringExtra("project_file"));
        RecyclerView remotesList = (RecyclerView) findViewById(R.id.remotes_list);
        final RemotesAdapter remotesAdapter = new RemotesAdapter(this, repo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(remotesList.getContext(),
                layoutManager.getOrientation());
        remotesList.addItemDecoration(dividerItemDecoration);

        remotesList.setLayoutManager(layoutManager);
        remotesList.setAdapter(remotesAdapter);

        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.new_remote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RemotesActivity.this);
                builder.setTitle("Add remote");

                View cloneView = LayoutInflater.from(RemotesActivity.this)
                        .inflate(R.layout.dialog_remote_add, null, false);

                final TextInputEditText file = (TextInputEditText) cloneView.findViewById(R.id.clone_name);
                final TextInputEditText remote = (TextInputEditText) cloneView.findViewById(R.id.clone_url);

                builder.setView(cloneView);
                builder.setPositiveButton(R.string.git_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Giiit.addRemote(RemotesActivity.this, repo, file.getText().toString(), remote.getText().toString());
                        remotesAdapter.add(file.getText().toString(), remote.getText().toString());
                    }
                });

                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });

        remotesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    button.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && button.isShown()) button.hide();
            }
        });
    }
}
