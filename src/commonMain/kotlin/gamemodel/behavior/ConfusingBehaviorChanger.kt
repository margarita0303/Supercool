package gamemodel.behavior

class ConfusingBehaviorChanger : BehaviorChanger {
    override fun getChangedBehavior(baseBehavior: Behavior): Behavior {
        return ChangedBehaviour(baseBehavior)
    }
}
