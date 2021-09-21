package engine.Threads;

import engine.entity.interfaces.IUpdate;

import java.util.ArrayList;

public class UpdateThread extends Thread{
        private final ArrayList<IUpdate> iUpdate;

        public UpdateThread(ArrayList<IUpdate> iUpdate) {
            this.iUpdate = iUpdate;
        }

        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            for(IUpdate iUp : iUpdate) {
                if(iUp == null) continue;
                iUp.update();
            }
        }
}
