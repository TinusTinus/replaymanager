/*
 * Copyright 2012, 2013 Martijn van de Rijdt 
 * 
 * This file is part of the Ultimate Marvel vs Capcom 3 Replay Manager.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with the Ultimate Marvel vs Capcom 3
 * Replay Manager. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mvdr.umvc3replayanalyser.video;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import lombok.extern.slf4j.Slf4j;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IError;
import com.xuggle.xuggler.IError.Type;

/**
 * Iterator for video frames.
 * 
 * This class is intended as a wrapper for the (kind of clunky) Xuggler API. A FrameIterator can be used as any other
 * iterator to iterate over the individual frames of a video.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
class FrameIterator implements Iterator<BufferedImage>, Closeable {
    /** Queue containing the images read from the input video. */
    private final Queue<BufferedImage> queue;

    /** Xuggler media reader. Null after it has been closed. */
    private IMediaReader reader;

    /** The error returned by the last call to reader.readPacket(); null if no such error has been encountered yet. */
    private IError error;

    /**
     * Constructor.
     * 
     * @param videoUrl
     *            URL of the video file from which to read frames
     */
    FrameIterator(String videoUrl) {
        super();
        this.queue = new LinkedList<>();
        this.reader = ToolFactory.makeReader(videoUrl);
        this.reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        this.reader.addListener(new MediaListenerAdapter() {
            /** {@inheritDoc} */
            @Override
            public void onVideoPicture(IVideoPictureEvent event) {
                if (log.isDebugEnabled()) {
                    log.debug("Decoded picture for timestamp " + event.getTimeStamp());
                }
                BufferedImage image = event.getImage();
                if (image == null) {
                    // This should not occur, since we called setBufferedImageTypeToGenerate on the reader.
                    log.warn("Buffered image not available for timestamp " + event.getTimeStamp());
                } else {
                    FrameIterator.this.queue.offer(image);
                    if (log.isDebugEnabled()) {
                        log.debug("Frame read: " + image);
                    }
                }
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext() {
        while (this.error == null && this.queue.isEmpty()) {
            if (this.reader == null) {
                throw new IllegalStateException("The FrameIterator has already been closed.");
            }

            // Read the next packet.
            // If any complete frames are found, onVideoPicture will be invoked.
            // onVideoPicture will then add the image(s) to the queue.
            this.error = this.reader.readPacket();
            log.debug("Read packet.");

            if (this.error != null) {
                // This was the last packet; log why.
                if (Type.ERROR_EOF == error.getType()) {
                    log.info("End of video file reached.");
                } else {
                    log.error(String.format("Unexpected Xuggle error: %s, type: %s. Stopping media reader.",
                            this.error, this.error.getType()));
                }

                // We will no longer read any packets; may as well close immediately.
                close();
            }
        }

        return !this.queue.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public BufferedImage next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return this.queue.poll();
    }

    /** {@inheritDoc} */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        if (this.reader != null) {
            this.reader.close();
            this.reader = null;
            log.info("Reader closed.");
        }
    }
}
