package com.example.hanoipic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnDragListener, View.OnClickListener {

    AppState appState;

    LinearLayout startPole;
    LinearLayout helpPole;
    LinearLayout targetPole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        appState = AppState.getInstance();

        findViews();

        setOnDragListeners();

        createTower();
    }

    private void setOnDragListeners() {
        startPole.setOnDragListener(this);
        helpPole.setOnDragListener(this);
        targetPole.setOnDragListener(this);
    }

    private void createTower()
    {
        GameAttributes ga = new GameAttributes();
        //fill starting pole with corresponding amount of disks
        for (int i = appState.getChosenLevel()-1; i >= 0; i--) {
            //create an text view of a disk, starting with the biggest

            ImageView imageDisk = new ImageView(getApplicationContext());
            imageDisk.setImageDrawable(getResources().getDrawable(ga.disksImgResourcesList[i]));
            imageDisk.setTag(""+i);


            imageDisk.setOnLongClickListener(this);

            //imageDisk.setOnTouchListener(new MyTouchListener());

            //imageDisk.setOnClickListener(this);
            //add view to the pole
            startPole.addView(imageDisk,0);
        }
    }

    private void findViews() {
        startPole = findViewById(R.id.start_pole_ll);
        helpPole = findViewById(R.id.help_pole_ll);
        targetPole = findViewById(R.id.target_pole_ll);
    }


    @Override
    public boolean onLongClick(View view) {

        //index of the view in a current container should be 0
        LinearLayout container = (LinearLayout) view.getParent();
        if(container.indexOfChild(view) == 0)
        {
            //move
            // Create a new ClipData.
            // This is done in two steps to provide clarity. The convenience method
            // ClipData.newPlainText() can create a plain text ClipData in one step.

            // Create a new ClipData.Item from the ImageView object's tag
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

            // Instantiates the drag shadow builder.
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            // Starts the drag
            view.startDrag(data//data to be dragged
                    , shadowBuilder //drag shadow
                    , view//local data about the drag and drop operation
                    , 0//no needed flags
            );

            //Set view visibility to INVISIBLE as we are going to drag the view
            view.setVisibility(View.INVISIBLE);
        }
        else
        {
            Toast.makeText(GameActivity.this, "can't move this", Toast.LENGTH_SHORT).show();
        }

        return true;
    }


    @Override
    public boolean onDrag(View view, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
            {
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                {
                    return true;
                }
                return false;
            }
            case DragEvent.ACTION_DRAG_ENTERED:
            {
                view.invalidate();
                return true;
            }
            case DragEvent.ACTION_DRAG_LOCATION:
            {
                // Ignore the event
                return true;
            }
            case DragEvent.ACTION_DRAG_EXITED:
            {
                view.invalidate();
                return true;
            }

            case DragEvent.ACTION_DROP:
            {
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);

                // Gets the text data from the item.
                String dragData = item.getText().toString();

                View draggedView = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) draggedView.getParent();
                LinearLayout acceptor = (LinearLayout) view;

                //remove the dragged view
                owner.removeView(draggedView);

                if(acceptor.getChildCount() == 0)
                {
                    acceptor.addView(draggedView, 0);
                }
                else  if(acceptor.getChildCount() > 0 && draggedIsSmallerThanTop(view, event))
                {
                    acceptor.addView(draggedView, 0);
                }
                else
                {
                    Log.d("ACTION_DROP", "last text: " + "empty");
                    Log.d("ACTION_DROP", "dragged text: " + dragData);
                    //Add the dragged view
                    owner.addView(draggedView, 0);
                }
                draggedView.setVisibility(View.VISIBLE);

                return true;
            }

            case DragEvent.ACTION_DRAG_ENDED:
            {
                view.invalidate();

                if (dropEventNotHandled(event))
                {
                    View vi = (View) event.getLocalState();
                    vi.setVisibility(View.VISIBLE);
                }

                if(isOrderCorrect())
                {
                    victoryAction();
                }
                // returns true; the value is ignored.
                return true;
            }

            // An unknown action type was received.
            default:
            {
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
            }
        }
        return false;
    }

    private void victoryAction() {

        //Toast.makeText(this, "ВЕРНО!", Toast.LENGTH_SHORT).show();

        String message = "Congrats! You made it! Want to play again?";

        AlertDialog.Builder a_builder = new AlertDialog.Builder(GameActivity.this);
        a_builder.setMessage(message)
                .setIcon(R.drawable.ic_hanoi)
                .setCancelable(false)
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Victory!");
        alert.show();
    }

    private boolean draggedIsSmallerThanTop(View view, DragEvent event) {
        //todo get ArrayList from view
        boolean isSmallerOnTop;
        LinearLayout acceptingContainer = (LinearLayout) view;
        //count views in acceptingContainer
        Log.d("draggedIsSmallerThanTop", "num of views: " + acceptingContainer.getChildCount());

        //get tag of a top view
        String tagOfTopView = "";
        tagOfTopView = tagOfTopView + acceptingContainer.getChildAt(0).getTag();
        int topViewValue = Integer.valueOf(tagOfTopView);
        Log.d("draggedIsSmallerThanTop", "tag of a top view: " + tagOfTopView);

        //get tag of dragged disk
        View v = (View) event.getLocalState();
        String tagOfDraggedDisk = v.getTag().toString();
        int draggedViewValue = Integer.valueOf(tagOfDraggedDisk);
        Log.d("draggedIsSmallerThanTop", "tag of a dargged view: " + tagOfDraggedDisk);

        if(draggedViewValue < topViewValue)
        {
            isSmallerOnTop = true;
        }
        else
        {
            isSmallerOnTop = false;
            Toast.makeText(GameActivity.this, "Can't build on smaller disk", Toast.LENGTH_SHORT).show();
        }

        return isSmallerOnTop;
    }

    private boolean dropEventNotHandled(DragEvent dragEvent)
    {
        return !dragEvent.getResult();
    }

    private boolean isOrderCorrect() {
        boolean isCorrect = false;

        if(targetPole.getChildCount() == appState.getChosenLevel()
            || helpPole.getChildCount() == appState.getChosenLevel())
        {
            isCorrect = true;
        }

        return isCorrect;
    }


    @Override
    public void onClick(View view) {

        //index of the view in a current container should be 0
        LinearLayout container = (LinearLayout) view.getParent();
        if(container.indexOfChild(view) == 0)
        {
            //move
            // Create a new ClipData.
            // This is done in two steps to provide clarity. The convenience method
            // ClipData.newPlainText() can create a plain text ClipData in one step.

            // Create a new ClipData.Item from the ImageView object's tag
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

            // Instantiates the drag shadow builder.
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            // Starts the drag
            view.startDrag(data//data to be dragged
                    , shadowBuilder //drag shadow
                    , view//local data about the drag and drop operation
                    , 0//no needed flags
            );

            //Set view visibility to INVISIBLE as we are going to drag the view
            view.setVisibility(View.INVISIBLE);
        }
        else
        {
            Toast.makeText(GameActivity.this, "can't move this", Toast.LENGTH_SHORT).show();
        }

        //return true;
    }



    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }



}
