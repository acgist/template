import cv2

writer = cv2.VideoWriter("player.mp4", cv2.VideoWriter_fourcc(*"mp4v"), 24, (320, 240))

class VideoPlayerWithTimeline:
    def __init__(self, video_path, events, show = True):
        self.cap = cv2.VideoCapture(video_path)
        self.show    = show
        self.label   = None
        self.fps     = self.cap.get(cv2.CAP_PROP_FPS)
        self.frames  = int(self.cap.get(cv2.CAP_PROP_FRAME_COUNT))
        self.width   = int(self.cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        self.height  = int(self.cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        self.events  = events
        self.paused  = False
        self.current = 0
        if self.show:
            cv2.namedWindow("player", cv2.WINDOW_AUTOSIZE)

    def draw_timeline_and_events(self, frame, current):
        h, w, _ = frame.shape
        total_duration  = self.frames / self.fps
        timeline_height = 20
        # 时间轴
        cv2.rectangle(frame, (0, h - timeline_height), (w, h), (0, 0, 0), -1)
        # 进度条
        progress_ratio = current / (self.frames / self.fps)
        progress_width = int(w * progress_ratio)
        cv2.rectangle(frame, (0, h - timeline_height), (progress_width, h), (0, 128, 0), -1)
        # 事件轴
        for event_time, label in self.events.items():
            x_pos = int((float(event_time) / total_duration) * w)
            cv2.line(frame, (x_pos, h - timeline_height), (x_pos, h), (0, 255, 255), 1)
            # text_size = cv2.getTextSize(f"{label[1][0]}", cv2.FONT_HERSHEY_SIMPLEX, 0.4, 1)[0]
            # cv2.putText(frame, f"{label[1][0]}", (x_pos - text_size[0] // 2, h - text_size[1] // 2), cv2.FONT_HERSHEY_SIMPLEX, 0.4, (255, 255, 255), 1)
        # 事件提示
        for event_time, label in self.events.items():
            if abs(current - float(event_time)) < 0.1:
                self.label = label
                label_prob = label[0]
                label_text = label[1]
            else:
                label_prob = self.label[0]
                label_text = self.label[1]
            cv2.rectangle(frame, (0, 0), (    100, 100), (255, 0, 0), -1)
            cv2.rectangle(frame, (w, 0), (w - 100, 100), (255, 0, 0), -1)
            cv2.putText(frame, f"{label_text[0]}", (20, 40), cv2.FONT_HERSHEY_SIMPLEX, 0.8, (255, 255, 255), 1)
            cv2.putText(frame, f"{label_text[1]}", (20, 80), cv2.FONT_HERSHEY_SIMPLEX, 0.8, (255, 255, 255), 1)
            cv2.putText(frame, f"{label_prob[0]:.2f}", (w - 80, 40), cv2.FONT_HERSHEY_SIMPLEX, 0.8, (255, 255, 255), 1)
            cv2.putText(frame, f"{label_prob[1]:.2f}", (w - 80, 80), cv2.FONT_HERSHEY_SIMPLEX, 0.8, (255, 255, 255), 1)
        return frame

    def run(self):
        while self.cap.isOpened():
            if not self.paused:
                ret, frame = self.cap.read()
                if not ret:
                    print("视频播放结束")
                    break
                self.current += 1
                current_time = self.current / self.fps
                frame = self.draw_timeline_and_events(frame, current_time)
                writer.write(frame)
                if not self.show:
                    continue
                cv2.imshow("player", frame)
            key = cv2.waitKey(25) & 0xFF
            if key == 27:
                break
            elif key == 32:
                self.paused = not self.paused
                if self.paused:
                    ret, frame = self.cap.read()
                    if ret:
                        self.cap.set(cv2.CAP_PROP_POS_FRAMES, self.current)
                        current_time = self.current / self.fps
                        frame = self.draw_timeline_and_events(frame, current_time)
                        cv2.imshow("player", frame)
        self.cap.release()
        writer.release()
        if self.show:
            cv2.destroyAllWindows()

# if __name__ == "__main__":
#     events = {"0.0": [[0.8013229370117188, 0.17911194264888763], ["eat", "sit"]], "1.38": [[0.389816552400589, 0.3113076984882355], ["smoke", "sit"]], "3.86": [[0.5994072556495667, 0.2693544626235962], ["smile", "smoke"]], "4.14": [[0.3655635118484497, 0.2910705506801605], ["sit", "smile"]], "4.69": [[0.6524499654769897, 0.25918930768966675], ["eat", "smoke"]], "5.52": [[0.7018769383430481, 0.2980852723121643], ["smoke", "eat"]], "6.9": [[0.49115413427352905, 0.46317896246910095], ["eat", "smoke"]], "7.17": [[0.8825661540031433, 0.08287396281957626], ["sit", "smoke"]], "11.86": [[0.6135467290878296, 0.3778855502605438], ["eat", "sit"]], "17.38": [[0.7200800180435181, 0.17436014115810394], ["situp", "eat"]], "27.86": [[0.4569638669490814, 0.42399293184280396], ["sit", "situp"]], "28.14": [[0.5672111511230469, 0.24247020483016968], ["eat", "sit"]], "30.07": [[0.5569610595703125, 0.3346380591392517], ["sit", "eat"]], "30.34": [[0.6217909455299377, 0.31607314944267273], ["eat", "sit"]], "30.9": [[0.5757467150688171, 0.42314180731773376], ["smoke", "eat"]], "31.17": [[0.9025974273681641, 0.08280882984399796], ["eat", "smoke"]], "32.28": [[0.6628077030181885, 0.3176807165145874], ["sit", "eat"]], "33.66": [[0.7617442607879639, 0.23824018239974976], ["eat", "sit"]], "37.79": [[0.9949042797088623, 0.0022431667894124985], ["sit", "eat"]], "42.48": [[0.7838479280471802, 0.2139788717031479], ["eat", "sit"]], "46.07": [[0.7140641808509827, 0.16080185770988464], ["smile", "eat"]], "49.93": [[0.9813111424446106, 0.013364477083086967], ["situp", "smile"]], "52.41": [[0.5397886633872986, 0.4545898735523224], ["sit", "situp"]], "54.07": [[0.9961736798286438, 0.00367223983630538], ["eat", "sit"]]}
#     video_file = "D:/download/video.mp4" 
#     player = VideoPlayerWithTimeline(video_file, events)
#     player.run()
#     writer.release()
