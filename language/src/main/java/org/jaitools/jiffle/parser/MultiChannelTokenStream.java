/* 
 *  Copyright (c) 2011, Michael Bedward. All rights reserved. 
 *   
 *  Redistribution and use in source and binary forms, with or without modification, 
 *  are permitted provided that the following conditions are met: 
 *   
 *  - Redistributions of source code must retain the above copyright notice, this  
 *    list of conditions and the following disclaimer. 
 *   
 *  - Redistributions in binary form must reproduce the above copyright notice, this 
 *    list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.   
 *   
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */   
package org.jaitools.jiffle.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.antlr.runtime.BufferedTokenStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;

/**
 * A {@code TokenStream} that can work with multiple active channels.
 * Adapted from ANTLR's {@link org.antlr.runtime.CommonTokenStream} class.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public final class MultiChannelTokenStream extends BufferedTokenStream {

    /**
     * The {@code List} of active channel indices. These should all be positive
     * and less than 99 (used by ANTLR to flag its hidden token channel).
     */
    protected final List<Integer> activeChannels = new ArrayList<Integer>();

    /**
     * Creates a new stream with the default ANTLR channel active.
     * 
     * @param tokenSource a lexer
     */
    public MultiChannelTokenStream(TokenSource tokenSource) {
        super(tokenSource);
        addActiveChannel(Token.DEFAULT_CHANNEL);
    }

    /**
     * Creates a new stream with the given channels active.
     * 
     * @param tokenSource a lexer
     * @param channels active channel indices
     */
    public MultiChannelTokenStream(TokenSource tokenSource, int[] channels) {
        super(tokenSource);
        for (int i = 0; i < channels.length; i++) {
            addActiveChannel(channels[i]);
        }
    }

    @Override
    public void consume() {
        if (p == -1) {
            setup();
        }
        p++;
        sync(p);
        while (!isActiveChannel(tokens.get(p).getChannel())) {
            p++;
            sync(p);
        }
    }

    /**
     * Looks backwards for the {@code kth} token on any of the active channels.
     * 
     * @param k number of active-channel tokens to scan over
     * @return the token
     */
    @Override
    protected Token LB(int k) {
        if (k == 0 || (p - k) < 0) {
            return null;
        }
        
        CommonTokenStream cs;

        int i = p;
        int n = 1;
        while (n <= k) {
            i = skipOffTokenChannelsReverse(i - 1);
            n++;
        }
        if (i < 0) {
            return null;
        }
        return tokens.get(i);
    }

    /**
     * Looks forwards for the {@code kth} token on any of the active channels.
     * 
     * @param k number of active-channel tokens to scan over
     * @return the token
     */
    @Override
    public Token LT(int k) {
        if (p == -1) {
            setup();
        }
        if (k == 0) {
            return null;
        }
        if (k < 0) {
            return LB(-k);
        }
        int i = p;
        int n = 1;
        while (n < k) {
            i = skipOffTokenChannels(i + 1);
            n++;
        }
        if (i > range) {
            range = i;
        }
        return tokens.get(i);
    }

    /**
     * Gets the index of the next token on an active channel, starting
     * from {@code pos}.
     * 
     * @param pos start token index
     * 
     * @return the token index 
     */
    protected int skipOffTokenChannels(int pos) {
        sync(pos);
        while (!isActiveChannel(tokens.get(pos).getChannel())) {
            pos++;
            sync(pos);
        }
        return pos;
    }

    /**
     * Gets the index of the next token on an active channel, starting
     * from {@code pos} and scanning backwards.
     * 
     * @param pos start token index
     * 
     * @return the token index 
     */
    protected int skipOffTokenChannelsReverse(int pos) {
        while (pos >= 0 && !isActiveChannel((tokens.get(pos)).getChannel())) {
            pos--;
        }
        return pos;
    }

    /**
     * Positions the stream at the first token on an active channel.
     */
    @Override
    protected void setup() {
        p = 0;
        sync(0);
        int i = 0;
        while (!isActiveChannel(tokens.get(i).getChannel())) {
            i++;
            sync(i);
        }
        p = i;
    }

    @Override
    public void setTokenSource(TokenSource tokenSource) {
        synchronized (activeChannels) {
            super.setTokenSource(tokenSource);
            removeAllActiveChannels();
            addActiveChannel(Token.DEFAULT_CHANNEL);
        }
    }

    /**
     * Adds a channel to those active.
     * @param channelNum the channel to add
     */
    public void addActiveChannel(int channelNum) {
        synchronized (activeChannels) {
            if (!isActiveChannel(channelNum)) {
                activeChannels.add(channelNum);
            }
        }
    }

    /**
     * Removes a channel from those active. It is safe to call this
     * method speculatively.
     * 
     * @param channelNum the channel to remove
     */
    public void removeActiveChannel(int channelNum) {
        synchronized (activeChannels) {
            Iterator<Integer> iter = activeChannels.iterator();
            while (iter.hasNext()) {
                if (iter.next() == channelNum) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * Tests if a channel is active.
     * 
     * @param channelNum the channel to test
     * @return {@code true} if the channel is active; {@code false otherwise}
     */
    public boolean isActiveChannel(int channelNum) {
        synchronized (activeChannels) {
            return activeChannels.contains(channelNum);
        }
    }

    /**
     * Removes all active channels.
     */
    public void removeAllActiveChannels() {
        synchronized (activeChannels) {
            activeChannels.clear();
        }
    }
}
