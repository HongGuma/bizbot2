package com.bizbot.bizbot2.home

import android.app.AlertDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.background.LoadDataJobService
import com.bizbot.bizbot2.background.SynchronizationData
import com.bizbot.bizbot2.room.AppDatabase
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.PermitModel
import kotlinx.android.synthetic.main.intro.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class IntroActivity : AppCompatActivity() {

    lateinit var introHandler:Handler
    val msg = Message()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)

        customDialog(this)

        val dbPath = baseContext.getDatabasePath("app_db")

        if(!dbPath.exists()){
            initData()
        }else{
            //nextActivity()
        }

        //val co = CoroutineTest()
        //co.main()

        introHandler = Handler(Looper.myLooper()!!){
            if(it.what == 1){
                nextActivity()
            }else{
                Toast.makeText(this,"에러 발생! 네트워크 설정을 확인해 주세요.",Toast.LENGTH_SHORT).show()
                exitProcess(0)
            }
            true
        }
        val JOB_ID = 1001
        val js = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceComponent = ComponentName(this, LoadDataJobService::class.java)
        val jobInfo = JobInfo.Builder(JOB_ID,serviceComponent)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE) //와이파이일때
            .setPeriodic(TimeUnit.MINUTES.toMillis(30)) //30분마다
            .build()
        js.schedule(jobInfo)

    }

    private fun customDialog(context: Context){
        val permitModel = PermitModel(0,false,"","",0,0)

        val builder = AlertDialog.Builder(context)
        val mView = LayoutInflater.from(context).inflate(R.layout.intro_dialog_layout,null)
        val yesBtn = mView.findViewById<Button>(R.id.dialog_yes_btn)
        val noBtn = mView.findViewById<Button>(R.id.dialog_no_btn)

        builder.setView(mView)
        val dialog = builder.create()
        dialog.show()

        val syncDate = Date(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        permitModel.syncTime = simpleDateFormat.format(syncDate)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        yesBtn.setOnClickListener {
            Toast.makeText(context,"비즈봇의 광고성 정보 수신동의가 처리되었습니다.(${permitModel.syncTime})",Toast.LENGTH_SHORT).show()
            permitModel.alert = true
            viewModel.insertPermit(permitModel)
            dialog.dismiss()
            msg.what = 1
            introHandler.sendMessage(msg)
        }

        noBtn.setOnClickListener {
            Toast.makeText(context,"비즈봇의 광고성 정보 수신거절이 처리되었습니다.(${permitModel.syncTime})",Toast.LENGTH_SHORT).show()
            permitModel.alert = false
            viewModel.insertPermit(permitModel)
            dialog.dismiss()
            msg.what = 1
            introHandler.sendMessage(msg)
        }

    }

    //서버에서 데이터 불러오기
    private fun initData() {
        GlobalScope.launch(Dispatchers.IO) {
            val synchronizedData = SynchronizationData(baseContext)
            msg.what = synchronizedData.SyncData()
            introHandler.sendMessage(msg)
        }
    }

    //다음 엑티비티로 이동
    private fun nextActivity(){
        intro_loading_layout.visibility = View.GONE
        intro_logo_layout.visibility = View.VISIBLE
        //1.5초후 메인 액티비티로 이동
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        },1500L)
    }



}