package nextflow.script

import spock.lang.Specification

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class ExecutionStackTest extends Specification {

    def setupSpec() {
        ExecutionStack.reset()
    }

    def 'should verify push and pop semantics' () {

        given:
        def s1 = Mock(BaseScript)
        def s2 = Mock(WorkflowDef)
        def s3 = Mock(WorkflowDef)

        expect:
        ExecutionStack.size()==0

        when:
        ExecutionStack.push(s1)
        then:
        ExecutionStack.size()==1
        ExecutionStack.current() == s1
        !ExecutionStack.withinWorkflow()
        when:
        ExecutionStack.push(s2)
        then:
        ExecutionStack.size()==2
        ExecutionStack.current() == s2
        ExecutionStack.withinWorkflow()

        when:
        ExecutionStack.push(s3)
        then:
        ExecutionStack.size()==3
        ExecutionStack.current() == s3
        ExecutionStack.withinWorkflow()

        when:
        def result = ExecutionStack.pop()
        then:
        result == s3
        ExecutionStack.size()==2
        ExecutionStack.current() == s2
        ExecutionStack.withinWorkflow()

        when:
        result = ExecutionStack.pop()
        then:
        result == s2
        ExecutionStack.size()==1
        ExecutionStack.current() == s1
        !ExecutionStack.withinWorkflow()

        when:
        result = ExecutionStack.pop()
        then:
        result == s1
        ExecutionStack.size()==0
        !ExecutionStack.withinWorkflow()

    }

    def 'should_validate_scope_prefix' ()  {
        given:
        def s1 = Mock(BaseScript)
        def s2 = Mock(WorkflowDef); s2.name >> null
        def s3 = Mock(WorkflowDef); s3.name >> 'foo'
        def s4 = Mock(WorkflowDef); s4.name >> 'bar'
        def s5 = Mock(WorkflowDef); s5.name >> 'baz'

        when:
        ExecutionStack.push(s1)
        ExecutionStack.push(s2)
        then:
        ExecutionStack.scopePrefix == null

        when:
        ExecutionStack.push(s3)
        then:
        ExecutionStack.scopePrefix == 'foo:'

        when:
        ExecutionStack.push(s4)
        then:
        ExecutionStack.scopePrefix == 'foo:bar:'

        when:
        ExecutionStack.push(s5)
        then:
        ExecutionStack.scopePrefix == 'foo:bar:baz:'
    }

}
