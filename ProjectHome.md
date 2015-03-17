![http://swing-task-monitor.googlecode.com/svn/trunk/doc/scr.jpg](http://swing-task-monitor.googlecode.com/svn/trunk/doc/scr.jpg)

Displays state of multiple runnig SwingWorkers with ability to cancel them.

# Usage #
## Include a dependency ##
You can include a following maven dependency:
```
<repository>
    <id>taskmonitor.repo</id>
    <name>Swing Task Monitor Repository</name>
    <url>http://swing-task-monitor.googlecode.com/svn/maven-repository/releases/</url>
    <releases>
        <enabled>true</enabled>
        <updatePolicy>never</updatePolicy>
    </releases>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
</repository>
...
<dependency>
    <groupId>org.taskmonitor</groupId>
    <artifactId>taskmonitor-main</artifactId>
    <version>1.3</version>
</dependency>
```

Or directly download [binaries](http://swing-task-monitor.googlecode.com/svn/maven-repository/releases/org/taskmonitor/taskmonitor-main/1.3/taskmonitor-main-1.3.jar), [sources](http://swing-task-monitor.googlecode.com/svn/maven-repository/releases/org/taskmonitor/taskmonitor-main/1.3/taskmonitor-main-1.3-sources.jar) and [javadoc](http://swing-task-monitor.googlecode.com/svn/maven-repository/releases/org/taskmonitor/taskmonitor-main/1.3/taskmonitor-main-1.3-javadoc.jar).

## Write code ##
Decorate workers with titles
```
    abstract class MyWorker<T, V>
        extends SwingWorker<T, V>
    {
        String title;

        public MyWorker( final String title )
        {
            this.title = title;
        }

        public String getTitle()
        {
            return this.title;
        }
    }
```
Subclass TaskQueue to use with decorated workers
```
    class MyTaskQueue
        extends TaskQueue<MyWorker>
    {
        @Override
        public String getTitle( final MyWorker worker )
        {
            return worker.getTitle();
        }
    }
```
Create components
```
        final TaskMonitor taskMonitor = new TaskMonitor( new MyTaskQueue() );

        final JFrame frame = new JFrame();
        frame.add( taskMonitor );
        // properly init and show components
        // ... 

        // now you can begin executing tasks
        taskMonitor.invoke( new MyWorker<BigInteger, Void>( "Counting grains of desert sand..." )
        {
            @Override
            protected BigInteger doInBackground()
            {
                return measureDesert();
            }
        } );
```

That's it!