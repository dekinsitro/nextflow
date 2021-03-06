/*
 * Copyright 2013-2019, Centre for Genomic Regulation (CRG)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nextflow.script

import static nextflow.ast.NextflowDSLImpl.*

import groovy.transform.CompileStatic
/**
 * Models the output of a process or a workflow component returning
 * more than one output channels
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@CompileStatic
class ChannelOut implements List {

    @Delegate List target

    private Map<String,Object> channels

    ChannelOut() {
        this.target = Collections.<Object>unmodifiableList(Collections.<Object>emptyList())
        this.channels = Collections.emptyMap()
    }

    ChannelOut(List c) {
        target = Collections.unmodifiableList(c)
        this.channels = Collections.emptyMap()
    }

    ChannelOut(LinkedHashMap<String,?> channels) {
        this.target = Collections.unmodifiableList(new ArrayList(channels.values()))
        this.channels = new LinkedHashMap(channels)
    }

    Set<String> getNames() { channels.keySet().findAll { !it.startsWith(OUT_PREFIX) }  }

    def getProperty(String name) {
        if( channels.containsKey(name) )
            return channels.get(name)
        else
            metaClass.getProperty(this,name)
    }

    def getFirst() { target[0] }

    def getSecond() { target[1] }

    def getThird() { target[2] }

    def getFourth() { target[3] }

    def getFifth() { target[4] }

    def getSixth() { target[5] }

    def getSeventh() { target[6] }

    def getEighth() { target[7] }

    def getNinth() { target[8] }

    def getTenth() { target[9] }

    /**
     * Helper method that `spread`
     *
     * @param args
     * @return
     */
    static List spread(Object[] args) {
        final result = new ArrayList(args.size()*2)
        for( int i=0; i<args.size(); i++ ) {
            if( args[i] instanceof ChannelOut ) {
                final list = (List)args[i]
                for( def el : list ) {
                    result.add(el)
                }
            }
            else {
                result.add(args[i])
            }
        }
        return result
    }

    static List spread(List args) {
        spread(args as Object[])
    }
}
