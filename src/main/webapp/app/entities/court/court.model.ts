import { BaseEntity, User } from './../../shared';

export const enum Region {
    'PetalingJaya',
    'KelanaJaya',
    'PutraJaya',
    'Kualalumpur',
    'SubangJaya',
    'Damansara',
    'USJ',
    'Puchong',
    'Nilai',
    'BukitBintang'
}

export class Court implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public rate?: string,
        public region?: Region,
        public courtImageContentType?: string,
        public courtImage?: any,
        public courtType?: BaseEntity,
        public user?: User,
        public courtLocation?: BaseEntity,
    ) {
    }
}
