package com.example.louise.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StoryOnlyActivity extends AppCompatActivity {

    private String story = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_only);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        story = "500年前，統治暗黑大陸的印琛(Jinzen)家族第七代傳人發生內戰，戰敗的蚋轅(Ruyen)決定尋找4000年前脫逃者所帶走的超原力，因而一路找尋到新世界。來到新世界的蚋轅為了在拳杉堡建立基地，殺害了無數安塔雅人與席奈人，並挑起兩族仇恨，擾亂了千年的和平。\n\n"
                + "蚋轅直到臨終前都沒有找到超原力，他與惡魔立約，如果能讓他找到超原力，他的靈魂願意成為惡魔的坐騎。惡魔於是結束了蚋轅的生命，將他化為一匹黑馬，並開始在拳杉堡四處潛伏，搜尋超原力。而席奈人與安塔亞人則聯合起來保護超原力，驅趕暗黑勢力。參與這場保衛戰的我們，都是席奈人與安塔雅人的後代，是兩族人共同的血脈。\n\n"
                + "不管你投入哪一方，只代表你決定在這場戰役中扮演甚麼任務。這場戰役，唯有靠我們通力合作，才可能守住拳杉堡、保護超原力不被奪走。\n\n";
        TextView textstory = (TextView) findViewById(R.id.story_storyonly);
        textstory.setText(story);
        textstory.setTextSize(18);

    }
}
