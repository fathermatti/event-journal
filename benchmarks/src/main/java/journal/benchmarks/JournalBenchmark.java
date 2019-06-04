package journal.benchmarks;

import java.nio.file.Path;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import disruptor.event.journal.Journal;
import disruptor.event.journal.JournalBufferWriter;
import disruptor.event.journal.JournalBuilder;
import disruptor.event.journal.JournalDirectoryCreater;
import disruptor.event.journal.JournalFileManager;
import disruptor.event.journal.persist.channel.ConcurrentFileChannelProvider;
import disruptor.event.journal.persist.channel.FileChannelPersister;
import disruptor.event.journal.persist.channel.FileChannelProvider;

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
public class JournalBenchmark {

    @State(Scope.Benchmark)
    public static class JournalEvent {
	
	Event event;
	
	@Setup
	public void setup() {

	    event = new Event();
	    
	    event.val1 = 10L;
	    event.val2 = 20L;
	    event.val3 = 30L;

	    event.val4 = 40;
	    event.val5 = 50;
	    event.val6 = 60;
	    
	    event.val7 = 70.7;
	    event.val8 = 80.8;
	    event.val9 = 90.9;
	    
	}
    }
    
    @State(Scope.Benchmark)
    public static class JournalState {
	private Journal<Event> journal;

	@Setup
	public void setup() {

	    JournalBufferWriter<Event> bufferWriter = new EventBufferWriter();
	    
	    journal = JournalBuilder.create()
		    .withBufferWriter(bufferWriter)
		    .build();
	}
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void throughput(JournalState state, JournalEvent event) 
    {
	state.journal.write(event.event);
    }    
    
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public void latency(JournalState state, JournalEvent event) 
    {
	state.journal.write(event.event);
    }
}
