package com.example.multiplechoice2.database

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
//class QuestionViewModel(val dao: DAO): ViewModel() {
class QuestionViewModel(): ViewModel() {

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance destroyed")
    }

    private val questionBank= listOf<Question>(
        Question(1,"Công ty Cổ phần F phát hành trái phiếu với thời hạn 5 năm, lãi suất ghi trên trái phiếu là 10%, trái phiếu có mệnh giá 100.000 đồng. Giá phát hành là 105.000 đồng. Bà Thảo đã mua loại trái phiếu này cách đây 3 năm và đã nhận tiền lãi 3 lần, bây giờ bà Thảo đã bán trái phiếu này với giá 95.000 đồng. Hãy xác định lãi suất hoàn vốn trong trường hợp này?","6.55%","10%","9.5%","10.5%",1,"",0)
                                                ,
        Question(2,"Một người muốn sau 5 năm nữa sẽ có 1 khoản tiền là 100 triệu đồng. Vậy ngày hôm nay (đầu kỳ) người đó phải gửi một khoản tiền là bao nhiêu.","90.91 triệu đồng","90 triệu đồng","62.09 triệu đồng","99 triệu đồng",3,"=100/(1+10%)^5",0)
                                                ,
        Question(3,"Ngân hàng BIDV công bố lãi suất 9%/năm trả lãi 2 lần/năm. Vậy lãi suất 6 tháng là bao nhiêu?","4.403%","9.000%","4.250%","day la dap an L",1,"=(1+9%)^(6/12)-1",0)
                                                ,
        Question(4,"Ngân hàng BIDV công bố lãi suất 3%/quý, tính lãi hàng quý. Hãy xác định lãi suất năm là bao nhiêu?","12.551%","12.000%","11.900%","12.500%",1,"=(1+3%)^(12/3)-1",0)
    )

    val dataSize= questionBank.size
    var quizCount=1

    private var currentIndex=0


    val currentQuestion: Question
        get() = questionBank[currentIndex]


    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun goBack() {
        if(currentIndex>0) {
            currentIndex -= 1
        }
    }

}