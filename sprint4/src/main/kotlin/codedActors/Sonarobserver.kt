/* Generated by AN DISI Unibo */ 
package codedActors

import Configuration
import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import it.unibo.kactor.sysUtil.createActor   //Sept2023
import kotlinx.coroutines.*

//User imports JAN2024
import org.eclipse.paho.client.mqttv3.MqttMessage
class Sonarobserver ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){
	val LimitDistance = 20
	val MINT = Configuration.conf.MINT
	var isStopped=false
	var lastStopTime:Long = 0;
	override fun getInitialState() : String{
		return "s0"
	}
	@OptIn(DelicateCoroutinesApi::class)
	override fun messageArrived(topic: String, msg: MqttMessage) {
		CommUtils.outgray("$msg")

		val parsedMsg = msg.toString().split(',')[4]
		CommUtils.outgray(parsedMsg)
		val distance = parsedMsg.toLongOrNull() ?: return
		CommUtils.outgray("$distance")

		if( distance < LimitDistance && !isStopped && (lastStopTime == 0.toLong() || ((System.currentTimeMillis() - lastStopTime) >= MINT)) ){

			lastStopTime = System.currentTimeMillis()
			isStopped=true
			val m1 = MsgUtil.buildEvent(name, "obstacle", "obstacle($msg)")
			println("   ${name} |  emit m1= $m1")
			CommUtils.outred("alarm")
			val event = CommUtils.buildEvent(name, "alarm","alarm(X)")
			GlobalScope.launch {
				emitLocalEvent(event)
			}
		}else{
			//println("   $name |  DISCARDS $Distance ")
			if(distance >= LimitDistance && isStopped){
				isStopped=false
				CommUtils.outgreen("resume")
				val event = CommUtils.buildEvent(name, "resume", "resume(X)")
				GlobalScope.launch {
					emitLocalEvent(event)
				}
			}
		}

	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						connectToMqttBroker( "tcp://localhost:1883" )
						subscribe(  "sonardata" ) //mqtt.subscribe(this,topic)

					}
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 
