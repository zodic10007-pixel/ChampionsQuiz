package com.almustashar.championsquiz;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    final int BG=Color.rgb(8,18,37), PANEL=Color.rgb(17,34,62), GOLD=Color.rgb(233,191,98), WHITE=Color.WHITE, MUTED=Color.rgb(185,198,219), GREEN=Color.rgb(35,135,93), RED=Color.rgb(160,53,65);
    LinearLayout root, body, answersBox;
    TextView title, subtitle, progress, questionText, prize, feedback;
    Button[] optionButtons=new Button[4];
    ArrayList<JSONObject> all=new ArrayList<>(), round=new ArrayList<>();
    int index=0, score=0, safe=0;
    boolean answered=false, started=false;
    int[] prizes={100,200,300,500,1000,2000,4000,8000,16000,32000,64000,125000,250000,500000,1000000};
    @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(BG); getWindow().setNavigationBarColor(BG); loadQuestions(); showHome();}
    int dp(float x){return (int)(x*getResources().getDisplayMetrics().density+0.5f);}
    GradientDrawable shape(int color,int stroke,int radius){GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); if(stroke!=0)d.setStroke(dp(1),stroke); return d;}
    TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setGravity(Gravity.CENTER);t.setTypeface(Typeface.create("sans-serif",bold?Typeface.BOLD:Typeface.NORMAL));t.setPadding(dp(8),dp(7),dp(8),dp(7));return t;}
    LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setGravity(Gravity.CENTER_HORIZONTAL);return l;}
    void base(String sub){root=column();root.setBackgroundColor(BG);root.setPadding(dp(18),dp(18),dp(18),dp(16));setContentView(root); TextView crest=text("✦",34,GOLD,true);root.addView(crest,new LinearLayout.LayoutParams(-1,dp(46))); title=text("تحدّي الأبطال",28,WHITE,true);root.addView(title);subtitle=text(sub,14,MUTED,false);root.addView(subtitle); View line=new View(this);line.setBackgroundColor(GOLD);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(dp(72),dp(3));lp.setMargins(0,dp(12),0,dp(14));root.addView(line,lp);}
    Button button(String label){Button b=new Button(this);b.setText(label);b.setTextColor(WHITE);b.setTextSize(16);b.setAllCaps(false);b.setTypeface(null,Typeface.BOLD);b.setBackground(shape(PANEL,GOLD,14));return b;}
    void loadQuestions(){try{InputStream in=getAssets().open("questions.json");byte[] bytes=new byte[in.available()];in.read(bytes);in.close();JSONArray a=new JSONArray(new String(bytes,StandardCharsets.UTF_8));for(int i=0;i<a.length();i++)all.add(a.getJSONObject(i));}catch(Exception e){}}
    void showHome(){base("اختبر معلوماتك في أعظم بطولة أوروبية"); TextView trophy=text("🏆",64,GOLD,true);root.addView(trophy,new LinearLayout.LayoutParams(-1,dp(105))); TextView desc=text("100 سؤال عن الأندية والنجوم والنهائيات\n15 سؤالًا في كل جولة • دون إنترنت",17,WHITE,true);desc.setLineSpacing(dp(5),1f);root.addView(desc,new LinearLayout.LayoutParams(-1,dp(100))); TextView best=text("أفضل نتيجة: "+getPreferences(0).getInt("best",0)+" نقطة",14,MUTED,false);root.addView(best); Button start=button("ابدأ المسابقة");LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(58));p.setMargins(0,dp(24),0,dp(10));root.addView(start,p);start.setOnClickListener(v->startGame()); Button about=button("طريقة اللعب");root.addView(about,new LinearLayout.LayoutParams(-1,dp(52)));about.setOnClickListener(v->new android.app.AlertDialog.Builder(this).setTitle("طريقة اللعب").setMessage("أجب عن 15 سؤالًا متدرج الصعوبة. كل إجابة صحيحة تمنحك نقاطًا. يمكنك استخدام حذف إجابتين مرة واحدة، أو الجمهور الافتراضي مرة واحدة. جميع الأسئلة تعمل دون إنترنت.").setPositiveButton("حسنًا",null).show());}
    void startGame(){if(all.size()<15){new android.app.AlertDialog.Builder(this).setMessage("تعذر تحميل الأسئلة.").setPositiveButton("حسنًا",null).show();return;} ArrayList<JSONObject> pool=new ArrayList<>(all);Collections.shuffle(pool);round=new ArrayList<>(pool.subList(0,15));index=0;score=0;safe=0;started=true;showQuestion();}
    void showQuestion(){answered=false;base("رحلتك نحو لقب بطل أوروبا"); JSONObject q=round.get(index);progress=text("السؤال "+(index+1)+" من 15",15,GOLD,true);root.addView(progress);prize=text("النقاط: "+score+"   |   جائزة المرحلة: "+prizes[index],14,MUTED,true);root.addView(prize); TextView step=text("◆ ━━━━━━━━━━━━━━━━━ ◆",13,GOLD,true);root.addView(step); questionText=text("",20,WHITE,true);questionText.setLineSpacing(dp(5),1f);questionText.setBackground(shape(PANEL,0,18));LinearLayout.LayoutParams qp=new LinearLayout.LayoutParams(-1,dp(140));qp.setMargins(0,dp(16),0,dp(14));root.addView(questionText,qp);try{questionText.setText(q.getString("question"));}catch(Exception e){}
        answersBox=column();root.addView(answersBox,new LinearLayout.LayoutParams(-1,-2)); JSONArray opts=q.optJSONArray("options");for(int i=0;i<4;i++){final int n=i;Button b=button(" ");optionButtons[i]=b;LinearLayout.LayoutParams op=new LinearLayout.LayoutParams(-1,dp(53));op.setMargins(0,dp(4),0,dp(4));answersBox.addView(b,op);b.setText((char)('أ'+i)+"  •  "+opts.optString(i));b.setOnClickListener(v->choose(n));}
        feedback=text("",14,MUTED,false);root.addView(feedback);LinearLayout tools=new LinearLayout(this);tools.setOrientation(LinearLayout.HORIZONTAL);tools.setGravity(Gravity.CENTER);LinearLayout.LayoutParams tl=new LinearLayout.LayoutParams(-1,dp(50));tl.setMargins(0,dp(8),0,0);root.addView(tools,tl);Button fifty=button("حذف إجابتين");tools.addView(fifty,new LinearLayout.LayoutParams(0,-1,1));Button audience=button("رأي الجمهور");LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(0,-1,1);ap.setMargins(dp(6),0,0,0);tools.addView(audience,ap);fifty.setOnClickListener(v->useFifty());audience.setOnClickListener(v->useAudience());}
    void choose(int n){if(answered)return;answered=true;JSONObject q=round.get(index);int correct=q.optInt("answer",0);for(int i=0;i<4;i++){optionButtons[i].setEnabled(false);optionButtons[i].setBackground(shape(i==correct?GREEN:(i==n?RED:PANEL),i==correct?GREEN:0,14));}if(n==correct){score+=prizes[index];feedback.setText("إجابة صحيحة! أحسنت.");feedback.setTextColor(GOLD);}else{feedback.setText("الإجابة الصحيحة: "+q.optJSONArray("options").optString(correct)+"\n"+q.optString("explanation"));feedback.setTextColor(WHITE);}Button next=button(index==14?"عرض النتيجة":"السؤال التالي");LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(52));p.setMargins(0,dp(8),0,0);root.addView(next,p);next.setOnClickListener(v->{if(index==14||n!=correct)finishGame();else{index++;showQuestion();}}); }
    void useFifty(){if(answered)return;JSONObject q=round.get(index);int correct=q.optInt("answer",0);int removed=0;for(int i=0;i<4;i++){if(i!=correct&&removed<2){optionButtons[i].setEnabled(false);optionButtons[i].setText("—");removed++;}}}
    void useAudience(){if(answered)return;int c=round.get(index).optInt("answer",0);String[] names={"أ","ب","ج","د"};StringBuilder s=new StringBuilder("تصويت الجمهور الافتراضي:\n");for(int i=0;i<4;i++)s.append(names[i]).append(": ").append(i==c?"70%":(i==((c+1)%4)?"15%":"7%")).append("\n");new android.app.AlertDialog.Builder(this).setTitle("رأي الجمهور").setMessage(s.toString()).setPositiveButton("عودة",null).show();}
    void finishGame(){int best=getPreferences(0).getInt("best",0);if(score>best)getPreferences(0).edit().putInt("best",score).apply();base("انتهت المسابقة");root.addView(text("🏆",60,GOLD,true),new LinearLayout.LayoutParams(-1,dp(100)));root.addView(text("نتيجتك",20,MUTED,true));root.addView(text(score+" نقطة",34,GOLD,true));root.addView(text("أفضل نتيجة: "+Math.max(score,best),16,WHITE,false));TextView msg=text("استمر في التحدي لتصبح بطل أوروبا!",16,MUTED,false);root.addView(msg);Button again=button("العب مرة أخرى");LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(56));p.setMargins(0,dp(20),0,dp(8));root.addView(again,p);again.setOnClickListener(v->startGame());Button home=button("الرئيسية");root.addView(home,new LinearLayout.LayoutParams(-1,dp(50)));home.setOnClickListener(v->showHome());}
}
