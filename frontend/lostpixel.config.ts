import type { CustomProjectConfig } from 'lost-pixel';

export const config: CustomProjectConfig = {
  ladleShots: {
    ladleUrl: 'http://172.17.0.1:61000',
  },
  lostPixelProjectId: process.env.LOST_PIXEL_PROJECT_ID,
  apiKey: process.env.LOST_PIXEL_API_KEY,
};
