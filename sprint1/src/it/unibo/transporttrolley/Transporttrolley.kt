/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023
class Transporttrolley ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
			val (HomeX, HomeY) = Pair(0, 0);
			val (IndoorX, IndoorY) = Pair(0, 4);
			val (ColdRoomX, ColdRoomY) = Pair(4, 3);
			var LoadTrolley : Long = 0;
				return { //this:ActionBasciFsm
				state("terminating") { //this:State
					action { //it:State
						CommUtils.outblack("$name ) Robot already engaged!")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("s0") { //this:State
					action { //it:State
						discardMessages = false
						CommUtils.outblack("$name ) has started, now let's engage the robot!")
						request("engage", "engage(ARG)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t05",targetState="terminating",cond=whenReply("engagerefused"))
				}	 
				state("isHome") { //this:State
					action { //it:State
						CommUtils.outblack("$name ) Robot in home waiting for trucks!")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("goinghome") { //this:State
					action { //it:State
						CommUtils.outblack("$name) Sending Robot to Home")
						request("moverobot", "moverobot(HomeX,HomeY)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t26",targetState="isHome",cond=whenReply("moverobotdone"))
				}	 
			}
		}
} 
