package com.xjc
import math._

/**
等额本息:
    每月月供额=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
    每月应还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
    每月应还本金=贷款本金×月利率×(1+月利率)^(还款月序号-1)÷〔(1+月利率)^还款月数-1〕
    总利息=还款月数×每月月供额-贷款本金

等额本金:
每月月供额=(贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率　　
每月应还本金=贷款本金÷还款月数　
每月应还利息=剩余本金×月利率=(贷款本金-已归还本金累计额)×月利率　　
每月月供递减额=每月应还本金×月利率=贷款本金÷还款月数×月利率　
总利息=〔(总贷款额÷还款月数+总贷款额×月利率)+总贷款额÷还款月数×(1+月利率)〕÷2×还款月数-总贷款额
*/

object loanCalc {

    def monthPay(monthRate:Double, funds:Double, loanTime:Int):Double = {
        val fee = funds * monthRate * pow(1 + monthRate, loanTime ) / ( pow(1 + monthRate, loanTime ) -1 )
        fee
    }


    def getMonthInterest(monthRate:Double, funds:Double, loanTime:Int, paySeq:Int):Double = {
        val monthInterest = funds * monthRate * ( pow(1 + monthRate, loanTime ) - pow(1 + monthRate, paySeq - 1) ) / (pow(1 + monthRate, loanTime) - 1)
        monthInterest
    }


    def getMonthCapital(monthRate:Double, funds:Double, loanTime:Int, paySeq:Int):Double = {
        val monthCapital = funds * monthRate * pow(1 + monthRate, paySeq - 1) / ( pow(1 + monthRate, loanTime) - 1 )
        monthCapital
    }

    def payDetail(monthRate:Double, funds:Double, loanTime:Int) {
        var (payTotalCapital, payTotalInterest, leftCapital) = (0d, 0d, 0d)

        println("payment detail:")
        println("%10s%10s%20s%20s%20s%20s%20s%20s".format("year", "month", "payEveryMonth", "monthInterest", "monthCapital", "payTotalInterest", "payTotalCapital", "leftCapital" ))

        for ( i <- 1 to loanTime ) {
            val monthInterest = getMonthInterest(monthRate, funds, loanTime, i)
            val monthCapital = getMonthCapital(monthRate, funds, loanTime, i)
            payTotalInterest += monthInterest
            payTotalCapital += monthCapital
            leftCapital = funds - payTotalCapital
            
            //implicit def double2str(num:Double):String = "%11.2f".format (num)

            //println(s"month_$i\t$monthInterest\t$monthCapital\t$payTotalInterest\t$payTotalCapital\t$leftCapital")
            var year_num = if ( i % 12 == 0) i / 12 else i / 12 + 1
            var month_num = if ( i % 12 == 0 ) 12 else i % 12
            println("%10s%10s%20.3f%20.3f%20.3f%20.3f%20.3f%20.3f".format("year_" + year_num, "month_" + month_num, monthInterest + monthCapital, monthInterest, monthCapital, payTotalInterest, payTotalCapital, leftCapital) )
        }
    }


    //等额本金
    def payDetail2(monthRate:Double, funds:Double, loanTime:Int) {
        var (payTotalCapital, payTotalInterest, leftCapital) = (0d, 0d, 0d)
        println("payment detail:")
        println("%10s%10s%20s%20s%20s%20s%20s%20s".format("year", "month", "payEveryMonth", "monthInterest", "monthCapital", "payTotalInterest", "payTotalCapital", "leftCapital" ))
    
        var monthCapital = funds / loanTime
    
        val allInfo =     
        for ( i <- 1 to loanTime ) {
            val monthInterest = (funds - monthCapital * (i-1) ) * monthRate
            val monthPay = monthCapital + monthInterest
            payTotalCapital += monthCapital
            payTotalInterest += monthInterest
            leftCapital = funds - payTotalCapital
    
            var year_num = if ( i % 12 == 0) i / 12 else i / 12 + 1
            var month_num = if ( i % 12 == 0 ) 12 else i % 12
            println("%10s%10s%20.3f%20.3f%20.3f%20.3f%20.3f%20.3f".format("year_" + year_num, "month_" + month_num, monthInterest + monthCapital, monthInterest, monthCapital, payTotalInterest, payTotalCapital, leftCapital) )
    
        }
    }

    def help() {
        println("usage:\n")
        println("    loanCalc funds funds_percent interest interest_rate loanTime[year] laonType[1:等额本息|2:等额本金]\n")
        println("eg:\n")
        println("房款2500000, 贷款7成, 贷款基准利率0.049, 利率优惠:0.85, 贷款20年, 等额本息方式")
        println("    loanCalc 2500000 0.7 0.049 0.85 20 1")
    }



    def main(args:Array[String]) {
        import scala.util.Try
        def parseDouble(s: String): Option[Double] = Try { s.toDouble }.toOption
        //implicit def str2int(num:String): Int = Integer.valueOf(num)

        var (funds, funds_percent, interest, interest_rate, loanTime, loanType) = (0.0d, 0.0d, 0.0d, 0.0d, 0, 0)
        args match {
            case Array(v1,v2,v3,v4,v5,v6) => {funds=parseDouble(v1).get
                                              funds_percent=parseDouble(v2).get
                                              interest=parseDouble(v3).get
                                              interest_rate=parseDouble(v4).get
                                              loanTime=v5.toInt
                                              loanType=v6.toInt}
            case _ => { help();
                        System.exit(-1)
            }
        }

        /**
        val funds = args(0)
        val rate = args[0]

        val funds = 2500000d 
        val percent = 0.7d
        */
        loanTime = loanTime * 12
        val loanFunds = funds * funds_percent
        val rate = interest * interest_rate
        val monthRate = rate / 12

        if (loanType == 1) {
            val avgMonthPay = monthPay(monthRate, loanFunds, loanTime)
            println("%20s:\t%20.3f".format("total interest", avgMonthPay * loanTime - loanFunds))
            println("%20s:\t%20.3f".format("every month paymenth", avgMonthPay))
            payDetail(monthRate, loanFunds, loanTime)
        } else if (loanType == 2) {
            var totalInterest1 = ((loanFunds / loanTime + loanFunds * monthRate) + (loanFunds / loanTime) * ( 1 + monthRate)) / 2 * loanTime - loanFunds
            println("%20s:\t%20.3f".format("total interest", totalInterest1))
            payDetail2(monthRate, loanFunds, loanTime)
        }

    }
}

